package battleship.GUI;

import battleship.Infrastructure.BattleShipPlacement;
import battleship.Infrastructure.CommMsg;
import battleship.Infrastructure.GameBoard;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

class DrawCanvas extends Canvas
{
    //variables
    private GameBoard myBoard;
    private GameBoard oppBoard;
    
    private BufferedImage hitMarker;
    private BufferedImage missMarker;
    private BufferedImage drawBuff;
    private Graphics2D drawGraphics;
    
    private int totWidth, totHeight, indWidth, indHeight,gridSize;
    private String board1, board2;
    
    BattleShipPlacement bsp;
    
    MouseWatcher mouse;
    DrawCanvasMouseMediator med;
    
    boolean needToUpdate = false;
    
    
    CommMsg msg;
    
    ObjectOutputStream out;
    ObjectInputStream in;
    int turn;
    //variables
    
    public DrawCanvas(ObjectInputStream i, ObjectOutputStream o, int ii) throws IOException
    {
        in = i;
        out = o;
        turn = ii;
        
        msg = new CommMsg();
        
        getSizes();
        initImages();
        
        setSize(totWidth, totHeight);
        bsp = new BattleShipPlacement();
        
        med = new DrawCanvasMouseMediator(this);
        mouse = new MouseWatcher(this, med);
    }
    
    @Override
    public void update(Graphics g)
    {
        //calculate the piece placement
        int x, y;
        x = (med.getMouseX()/50)*50;
        y = (med.getMouseY()/50)*50;
        if(bsp.shipsRemaining()) {
            if(x < 50)
                x = 50;
            else if((x+(bsp.getCurrentShipSize()*50)) > 500)
                x = 500 - (bsp.getCurrentShipSize()*50);
            if(y < 50)
                y = 50;
        }
        else {
            if(x < 600)
                x = 600;
            else if((x+(bsp.getCurrentShipSize()*50)) > 1050)
                x = 1050 - (bsp.getCurrentShipSize()*50);
            if(y < 50)
                y = 50;
        }
        
        if(turn == 1 && !bsp.shipsRemaining())
            waitForAndHandleMove();
        
        if(needToUpdate) {
            needToUpdate = false;
            turn = 1;
        }
            
    
    
        
        //draw everything to the buffer
        drawGraphics.clearRect(0, 0, 407, 333);
        myBoard.render(drawGraphics, 0, 0);
        oppBoard.render(drawGraphics, 550, 0);
        bsp.render(drawGraphics, x, y);

        //draw the buffer
        paint(g);
    }
    
    @Override
    public void paint(Graphics g)
    {
        g.drawImage(drawBuff, 0, 0, this);
    }
    
    private void initImages() throws IOException
    {
        myBoard = new GameBoard(board1);
        oppBoard = new GameBoard(board2);
        
        hitMarker = ImageIO.read(new File("hitMarker.png"));
        missMarker = ImageIO.read(new File("missMarker.png"));
        
        drawBuff = new BufferedImage(1050, 500, BufferedImage.TYPE_INT_ARGB);
        drawGraphics = drawBuff.createGraphics();
        drawGraphics.setBackground(new Color(0, 0, 0, 0));        
    }

    public void reactToClick() throws IOException, ClassNotFoundException {
        
        int x = (med.getMouseX()/50)*50;
        int y = (med.getMouseY()/50)*50;
        
        //This is just for placing the initial ships
        if(bsp.shipsRemaining()) {
            if(x < 50)
                x = 50;
            else if((x+(bsp.getCurrentShipSize()*50)) > 500)
                x = 500 - (bsp.getCurrentShipSize()*50);
            if(y < 50)
                y = 50;
            
            myBoard.placePiece(bsp.getPiece().getImage(), bsp.getPiece().getSize(), x/50, y/50);
            bsp.nextShip();
        }
        
        //This is the main case
        else {
            if(x < 600)
                x = 600;
            else if((x+(bsp.getCurrentShipSize()*50)) > 1050)
                x = 1050 - (bsp.getCurrentShipSize()*50);
            if(y < 50)
                y = 50;
            
            //Here I will send a packet and wait for a response. A response
            //will include whether or not an opponents ship was hit. If it was
            //It will draw something on the map(maybe a X?) to let the user know
            //they hit
            
            x = (x-550)/50;
            y = y/50;
            msg = new CommMsg(x, y);
            out.writeObject(msg);
            msg = (CommMsg) in.readObject();
            
            System.out.println("Sending the move: X "+x+ " Y " + y);
            
            //if(myBoard.hit((x-550)/50, y/50) == true) {
            if(msg.hit == true) {
                System.out.println("I hit them... better take note X");
                oppBoard.placePiece(hitMarker, 0, x, y);
                needToUpdate = true;
            }
            else {
                System.out.println("I missed them... better take note []");
                oppBoard.placePiece(missMarker, 0, x, y);
                needToUpdate = true;
            }
            
        }
    }

    private void getSizes() throws FileNotFoundException, IOException {
        Scanner scan = new Scanner(new File("settings.txt"));
        board1 = scan.nextLine();
        board2 = scan.nextLine();
        totWidth = scan.nextInt();
        totHeight = scan.nextInt();
        indWidth = scan.nextInt();
        indHeight = scan.nextInt();
        gridSize = scan.nextInt();
    }
    
    private void waitForAndHandleMove() {
        //recieve a message
            try {
                //the user is now waiting for the oppenents move to come through
                   msg = (CommMsg) in.readObject();
            } catch (IOException ex) {
                Logger.getLogger(DrawCanvas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DrawCanvas.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            
            System.out.println("Recieved the move: X "+msg.x+ " Y " + msg.y);
            //evaluate the message
            if(myBoard.hit(msg.x, msg.y) == true) {
                System.out.println("They hit. Printing X on myBoard");
                myBoard.placePiece(hitMarker, 0, msg.x, msg.y);
                msg.hit = true;
            }
            else {
                System.out.println("They Missed. Printing [] on myBoard");
                myBoard.placePiece(missMarker, 0, msg.x, msg.y);
                msg.hit = false;
            }
            
            //send the message
            try {
                out.writeObject(msg);
            } catch (IOException ex) {
                Logger.getLogger(DrawCanvas.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            turn = 0;
    }
}