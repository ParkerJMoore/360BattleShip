package battleship.GUI;

import battleship.Infrastructure.BattleShipPlacement;
import battleship.Infrastructure.GameBoard;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

class DrawCanvas extends Canvas
{
    //variables
    GameBoard board;
    private BufferedImage drawBuff;
    private Graphics2D drawGraphics;
    
    BattleShipPlacement bsp;
    
    MouseWatcher mouse;
    DrawCanvasMouseMediator med;
    //variables
    
    
    
    public DrawCanvas() throws IOException
    {
        initImages();
        
        setSize(500, 500);
        bsp = new BattleShipPlacement();
        
        med = new DrawCanvasMouseMediator(this);
        mouse = new MouseWatcher(this, med);
    }
    
    @Override
    public void update(Graphics g)
    {
        drawGraphics.clearRect(0, 0, 407, 333);
        
        board.render(drawGraphics, 0, 0);
        int x, y;
        x = (med.getMouseX()/50)*50;
        y = (med.getMouseY()/50)*50;
        if((med.getMouseX()/50)*50 < 50)
            x = 50;
        if((med.getMouseY()/50)*50 < 50)
            y = 50;
        
        bsp.render(drawGraphics, x, y);
        paint(g);
    }
    
    @Override
    public void paint(Graphics g)
    {
        g.drawImage(drawBuff, 0, 0, this);
    }
    
    private void initImages() throws IOException
    {
        board = new GameBoard();
        drawBuff = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        drawGraphics = drawBuff.createGraphics();
        drawGraphics.setBackground(new Color(0, 0, 0, 0));        
    }

    public void reactToClick() {
        if(bsp.shipsRemaining()) {
            board.placePiece(bsp.getPiece(), med.getMouseX(), med.getMouseY());
            bsp.nextShip();
        }
        
        else
            System.out.println("No ships left");
        
    }
    
    
}