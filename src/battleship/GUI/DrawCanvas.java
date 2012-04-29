package battleship.GUI;

import battleship.Infrastructure.BattleShipPlacement;
import battleship.Infrastructure.CommMsg;
import battleship.Infrastructure.GameBoard;
import battleship.Infrastructure.NetworkMedium;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

class DrawCanvas extends Canvas
{
    //variables
    private GameBoard myBoard;
    private GameBoard oppBoard;
    
    private BufferedImage hitMarker;
    private BufferedImage missMarker;
    private BufferedImage drawBuff;
    private Graphics2D drawGraphics;

    
    private int indWidth;
    private int indHeight;
    private int gridSize;
    private int totWidth, totHeight;
    private String board1, board2;
    
    BattleShipPlacement bsp;
    
    MouseWatcher mouse;
    DrawCanvasMouseMediator med;
    
    boolean needToUpdate = false;
    
    
    NetworkMedium netMed;
    
    ObjectOutputStream out;
    ObjectInputStream in;
    int turn;
    
    MainWindowListener mwl;
    //variables
    
    public DrawCanvas(MainWindowListener m) throws IOException
    {
        turn = -1;
        mwl = m;

        netMed = new NetworkMedium(new CommMsg(), out, in);
        
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
        int x = calcX();
        int y = calcY();
        
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
        if(turn == -1)
            JOptionPane.showMessageDialog(null, "Please select Join/Select from"
                    + " the Match menu before making a move.");
        else {
            int x = calcX()/50;
            int y = calcY()/50;

            //This is just for placing the initial ships
            if(bsp.shipsRemaining()) {
                myBoard.placePiece(bsp.getPiece().getImage(), bsp.getPiece().getSize(), x, y);
                bsp.nextShip();
            }

            else {
                //offsetting the calculation
                x = x-11;
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
    
    public int calcX()
    {
        int x;
        x = (med.getMouseX()/50)*50;
        
        if(bsp.shipsRemaining()) {
            if(x < 50)
                x = 50;
            else if((x+(bsp.getCurrentShipSize()*50)) > 500)
                x = 500 - (bsp.getCurrentShipSize()*50);
        }
        else {
            if(x < 600)
                x = 600;
            else if((x+(bsp.getCurrentShipSize()*50)) > 1050)
                x = 1050 - (bsp.getCurrentShipSize()*50);
        }
        
        return x;
    }
    
    public int calcY()
    {
        int y = (med.getMouseY()/50)*50;
        
        if(y<50)
            y = 50;
        
        return y;
    }
    
    public void setStreams(ObjectInputStream i, ObjectOutputStream o, int ii)
    {
        in = i;
        out = o;
        turn = ii;
        
        netMed = new NetworkMedium(new CommMsg(), out, in);
    }
}