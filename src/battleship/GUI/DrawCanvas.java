package battleship.GUI;

import battleship.Infrastructure.BattleShipPlacement;
import battleship.Infrastructure.CommMsg;
import battleship.Infrastructure.GameBoard;
import battleship.Infrastructure.NetworkMedium;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

class DrawCanvas extends Canvas
{
    /***************INITIALIZATIONS*************/
    //variables
    private GameBoard myBoard;
    private GameBoard oppBoard;
    
    private BufferedImage hitMarker;
    private BufferedImage missMarker;
    private BufferedImage drawBuff;
    private BufferedImage menuBuff;
    private BufferedImage boardBuff;
    private Graphics2D boardGraphics;
    private Graphics2D drawGraphics;

    BattleShipPlacement bsp;
    
    MouseWatcher mouse;
    DrawCanvasMouseMediator med;
    
    boolean needToUpdate = false;
    
    
    NetworkMedium netMed;
    
    ObjectOutputStream out;
    ObjectInputStream in;
    int turn;
    
    MainWindowListener mwl;
    /***************INITIALIZATIONS*************/
    
    
    /**************CONSTRUCTOR AND DEPENDENCIES************/
    public DrawCanvas(MainWindowListener m) throws IOException
    {
        turn = -1;
        mwl = m;

        netMed = new NetworkMedium(new CommMsg(), out, in);
        
        initImages();
        
        setSize(1000, 500);
        bsp = new BattleShipPlacement();
        
        med = new DrawCanvasMouseMediator(this);
        mouse = new MouseWatcher(this, med);
    }
    
     private void initImages() throws IOException
    {
        myBoard = new GameBoard("board.png");
        oppBoard = new GameBoard("board.png");
        
        hitMarker = ImageIO.read(new File("hitMarker.png"));
        missMarker = ImageIO.read(new File("missMarker.png"));
        
        
        drawBuff = new BufferedImage(1050, 500, BufferedImage.TYPE_INT_ARGB);
        drawGraphics = drawBuff.createGraphics();
        
        drawGraphics.setColor(Color.BLACK);
        drawGraphics.setBackground(new Color(0, 0, 0, 0));
        drawGraphics.setFont(new Font("serif", Font.BOLD, 15));
        
        boardBuff = ImageIO.read(new File("battleBoard.png"));
        boardGraphics = boardBuff.createGraphics();
    }
     
     //should really only ever be called once.
     public void setStreams(ObjectInputStream i, ObjectOutputStream o, int ii)
    {
        in = i;
        out = o;
        turn = ii;
        
        netMed = new NetworkMedium(new CommMsg(), out, in);
    }
    /***************END CONSTRUCTOR AND DEPENDENCIES*************/
     
    
     
    /****************DRAWING AND PAINTING**************************/ 
    @Override
    public void update(Graphics g)
    {
        //Clear the screen
        drawGraphics.clearRect(0, 0, 1000, 500);
        drawGraphics.drawImage(boardBuff, 0, 0, null);
        /*
        //Draw appropriate images
        if(turn == -1)
            menuState();
        else
        */
        gameState();

        //paint them to the screen
        paint(g);
    }
    
    @Override
    public void paint(Graphics g)
    {
        g.drawImage(drawBuff, 0, 0, this);
    }
    
     public void menuState()
    {
        //draw all the menu stuff that we want
        drawGraphics.drawString("Please Select a turn", 500, 200);
    }
    
    public void gameState()
    {
         //calculate the piece placement
        int x = calcX();
        int y = calcY();
        
        if(turn == 1 && !bsp.shipsRemaining())
            waitForAndHandleMove();
        
        if(needToUpdate) {
            needToUpdate = false;
            turn = 1;
        }
            
        //draw everything to the buffer
        myBoard.render(drawGraphics, 150, 50);
        oppBoard.render(drawGraphics, 550, 50);
        bsp.render(drawGraphics, x, y);
    }
    /****************END DRAWING AND PAINTING**************************/
    
   
    /*********************INTERPRETING INPUT AND NETWORK INTERACTION**********/
    public void menuClick()
    {
        int x = calcX()/30;
        int y = calcY()/30;
            
        System.out.println("Menu: The position is X: " + (x-4) + "Y: " + y);
        //if(x < /*upper bound*/ && x > /*lowerbound*/ && y < /*.....*/)
        /*
        boolean createClicked;
        boolean joinClicked;
        
        if(createClicked)
            //do server setup
        else if(joinClicked)
            //do client setup
        */  
    }
    
    public void reactToClick() throws IOException, ClassNotFoundException {
        if(turn == -1)
            JOptionPane.showMessageDialog(null, "Please select Join/Select from"
                    + " the Match menu before making a move.");
        else {
            int x = (calcX()/30)-5;
            int y = (calcY()/30)-1;
            
            //This is just for placing the initial ships
            if(bsp.shipsRemaining()) {
                myBoard.placePiece(bsp.getPiece().getImage(), bsp.getPiece().getSize(), x, y);
                bsp.nextShip();
            }

            else {
                //offsetting the calculation
                x = x-13;
                System.out.println("Sending X: " + x + " Y: " + y);
                netMed.setMove(x, y);
                netMed.send();
                netMed.recieve();

                if(netMed.hit() == true) {
                    oppBoard.placePiece(hitMarker, 0, x, y);
                    needToUpdate = true;
                }
                else {
                    oppBoard.placePiece(missMarker, 0, x, y);
                    needToUpdate = true;
                }
            }
        }

    }
    
    
    private void waitForAndHandleMove() {
        //recieve a message
        netMed.recieve();

        //evaluate the message
        if(myBoard.hit(netMed.getMoveX(), netMed.getMoveY()) == true) {
            myBoard.placePiece(hitMarker, 0, netMed.getMoveX(), netMed.getMoveY());
            netMed.setHit(true);
        }
        else {
            myBoard.placePiece(missMarker, 0, netMed.getMoveX(), netMed.getMoveY());
            netMed.setHit(false);
        }

        //send the message
        netMed.send();
        
        //change the turn.
        turn = 0;
    }
    /*********************END INTERPRETING INPUT AND NETWORK INTERACTION*****/
    
    
    /********************HELPER METHODS********************************/
    public int calcX()
    {
        int x;
        x = (med.getMouseX()/30)*30;
        
        if(bsp.shipsRemaining()) {
            if(x < 150)
                x = 150;
            else if((x+(bsp.getCurrentShipSize()*30)) > 450)
                x = 450 - (bsp.getCurrentShipSize()*30);
        }
        else {
            if(x < 550)
                x = 550;
            else if((x+(bsp.getCurrentShipSize()*30)) > 850)
                x = 850 - (bsp.getCurrentShipSize()*30);
            
            //to compensate for it being 30 by 30
            if(x != 550 || x != 850 - (bsp.getCurrentShipSize()*30)) {
                while((x-550) %30 != 0) {
                    x++;
                }
            }
        }
        return x;
    }
    
    public int calcY()
    {
        int y = (med.getMouseY()/30)*30;
        
        if(y < 50)
            y = 60;
        else if(y > 350)
            y = 330;
        
        return y-10;
    }
    /********************END HELPER METHODS********************************/
   
}