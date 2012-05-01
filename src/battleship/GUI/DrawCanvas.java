package battleship.GUI;

import battleship.Infrastructure.BattleShipPlacement;
import battleship.Infrastructure.CommMsg;
import battleship.Infrastructure.GameBoard;
import battleship.Infrastructure.NetworkMedium;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private Graphics2D drawGraphics;
    
    private BufferedImage menuBuff;
    public  Graphics2D menuGraphics;
    
    private BufferedImage winBuff;
    private BufferedImage loseBuff;

    private BufferedImage boardBuff;
    private Graphics2D boardGraphics;

    BattleShipPlacement bsp;
    
    MouseWatcher mouse;
    DrawCanvasMouseMediator med;
    
    boolean needToUpdate = false;
    
    
    NetworkMedium netMed;
    
    ObjectOutputStream out;
    ObjectInputStream in;
    int turn;
    
    InetAddress ownIP;
    
    MainWindowListener mwl;
    /***************INITIALIZATIONS*************/
    
    
    /**************CONSTRUCTOR AND DEPENDENCIES************/
    public DrawCanvas(MainWindowListener m) throws IOException
    {
        
        //Get the hosts IP address
        try {
            ownIP = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(DrawCanvas.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
        //create the boards for game play
        myBoard = new GameBoard("board.png");
        oppBoard = new GameBoard("board.png");
        
        //hit and miss markers for game play
        hitMarker = ImageIO.read(new File("hitMarker.png"));
        missMarker = ImageIO.read(new File("missMarker.png"));
        
        //the buffer that everything will be drawn to
        drawBuff = new BufferedImage(1050, 500, BufferedImage.TYPE_INT_ARGB);
        drawGraphics = drawBuff.createGraphics();
        
        //setting up the draw graphics for drawing and writing
        drawGraphics.setColor(Color.YELLOW);
        drawGraphics.setBackground(new Color(0, 0, 0, 0));
        drawGraphics.setFont(new Font("serif", Font.BOLD, 15));
        
        //the background image
        boardBuff = ImageIO.read(new File("battleBoard.png"));
        boardGraphics = boardBuff.createGraphics();
        boardGraphics.setColor(Color.YELLOW);
        boardGraphics.setBackground(new Color(0, 0, 0, 0));
        boardGraphics.setFont(new Font("serif", Font.BOLD, 15));
        
        //the menu that will be used at the beginning
        menuBuff = ImageIO.read(new File("titlescreen.png"));
        
        winBuff = ImageIO.read(new File("win.png"));
        loseBuff = ImageIO.read(new File("lose.png"));
        
        menuGraphics = menuBuff.createGraphics();
        menuGraphics.setColor(Color.YELLOW);
        menuGraphics.setBackground(new Color(0, 0, 0, 0));
        menuGraphics.setFont(new Font("serif", Font.BOLD, 15));
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
        
        //Draw appropriate images
        if(turn == -1)
            menuState();
        else if(turn <= -2)
            endState();
        else
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
        drawGraphics.drawImage(menuBuff, 0, 0, null);
        drawGraphics.drawString(netMed.getIP(), 5, 490);
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
        drawGraphics.drawImage(boardBuff, 0, 0, null);
        myBoard.render(drawGraphics, 150, 50);
        oppBoard.render(drawGraphics, 550, 50);
        bsp.render(drawGraphics, x, y);
        
        //drawing your ship information
        drawGraphics.drawImage(bsp.getShipImage(0), 5, 55, null);
        drawGraphics.drawString(Integer.toString(myBoard.shipsLeft(0)), 100, 75);
        
        drawGraphics.drawImage(bsp.getShipImage(1), 5, 95, null);
        drawGraphics.drawString(Integer.toString(myBoard.shipsLeft(1)), 100, 115);
        
        //drawing enemy ship information
        drawGraphics.drawImage(bsp.getShipImage(0), 850, 55, null);
        drawGraphics.drawString(Integer.toString(oppBoard.shipsLeft(0)), 955, 75);
        
        drawGraphics.drawImage(bsp.getShipImage(1), 850, 95, null);
        drawGraphics.drawString(Integer.toString(oppBoard.shipsLeft(1)), 955, 115);
        
        //write the appropriate status
        if(bsp.shipsRemaining() && turn == 0)
            drawGraphics.drawString("Please place your ships on the board."
                    , 349, 450);
        else if(bsp.shipsRemaining() && turn == 1) {
            drawGraphics.drawString("Please place your ships on the board and"
                    , 349, 450);
            drawGraphics.drawString("wait for opponent to make their first move."
                    , 349, 465);
        }
        else if(turn == 0)
            drawGraphics.drawString("Please make a move."
                    , 416, 450);
        else
            drawGraphics.drawString("Waiting for opponent to make their move."
                    , 330, 450);
    }
    
    public void endState()
    {
        if(turn == -2)
            drawGraphics.drawImage(winBuff, 0, 0, null);
        else
            drawGraphics.drawImage(loseBuff, 0, 0, null);
        
    }
    /****************END DRAWING AND PAINTING**************************/
    
   
    /*********************INTERPRETING INPUT AND NETWORK INTERACTION**********/
    public void reactToClick() throws IOException, ClassNotFoundException {
        if(turn != -1) 
        {
            //offset coordinates for players board
            int x = (calcX()/30)-5;
            int y = (calcY()/30)-1;
            
            //This is just for placing the initial ships
            if(bsp.shipsRemaining()) {
                myBoard.placePiece(bsp.getPiece().getImage(), bsp.getPiece().getSize(),
                        bsp.getPiece().getID(), x, y);
                bsp.nextShip();
            }

            else {
                //offsetting the calculation
                x = x-13;
                netMed.setMove(x, y, 0);
                netMed.send();
                netMed.recieve();

                if(netMed.getID() != 0) {
                    oppBoard.placePiece(hitMarker, 0, 0, x, y);
                    needToUpdate = true;
                    if(netMed.win())
                        turn = -2;
                    oppBoard.removeOne((netMed.getID()-1));
                }
                else {
                    oppBoard.placePiece(missMarker, 0, 0, x, y);
                    needToUpdate = true;
                }
            }
        }

    }
    
    
    private void waitForAndHandleMove() {
        //recieve a message
        netMed.recieve();

        //evaluate the message
        int k = myBoard.hit(netMed.getMoveX(), netMed.getMoveY());
        if(k != 0) {
            myBoard.placePiece(hitMarker, 0, 0, netMed.getMoveX(), netMed.getMoveY());
            netMed.setHit(true);
            netMed.setMove(0,0,k);
            System.out.println(netMed.getID());
            
            if(myBoard.gameOver()) {
                netMed.setWin(true);
                turn = -3;
            }
            else 
                turn = 0;
        }
        else {
            myBoard.placePiece(missMarker, 0, 0, netMed.getMoveX(), netMed.getMoveY());
            netMed.setHit(false);
            netMed.setWin(false);
            netMed.setMove(0,0,0);
            turn = 0;
        }

        //send the message
        netMed.send();
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
                while((x-550) % 30 != 0) {
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
