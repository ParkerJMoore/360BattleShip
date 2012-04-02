package battleship.GUI;

import battleship.Infrastructure.BattleShipPlacement;
import battleship.Infrastructure.GameBoard;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

class DrawCanvas extends Canvas
{
    //variables
    private GameBoard myBoard;
    private GameBoard oppBoard;
    private BufferedImage drawBuff;
    private Graphics2D drawGraphics;
    
    private int totWidth, totHeight, indWidth, indHeight,gridSize;
    private String board1, board2;
    
    BattleShipPlacement bsp;
    
    MouseWatcher mouse;
    DrawCanvasMouseMediator med;
    //variables
    
    
    
    public DrawCanvas() throws IOException
    {
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
        
        drawBuff = new BufferedImage(1050, 500, BufferedImage.TYPE_INT_ARGB);
        drawGraphics = drawBuff.createGraphics();
        drawGraphics.setBackground(new Color(0, 0, 0, 0));        
    }

    public void reactToClick() {
        
        int x = (med.getMouseX()/50)*50;
        int y = (med.getMouseY()/50)*50;
        
        if(bsp.shipsRemaining()) {
            if(x < 50)
                x = 50;
            else if((x+(bsp.getCurrentShipSize()*50)) > 500)
                x = 500 - (bsp.getCurrentShipSize()*50);
            if(y < 50)
                y = 50;
            
            myBoard.placePiece(bsp.getPiece(), x/50, y/50);
            bsp.nextShip();
        }
        else {
            if(x < 600)
                x = 600;
            else if((x+(bsp.getCurrentShipSize()*50)) > 1050)
                x = 1050 - (bsp.getCurrentShipSize()*50);
            if(y < 50)
                y = 50;
            
            System.out.println("x: " + (x-550)/50);
            
            if(myBoard.hit((x-550)/50, y/50) == true)
                System.out.println("Hit!");
            else
                System.out.println("Miss!");
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
}