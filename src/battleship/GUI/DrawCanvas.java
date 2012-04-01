package battleship.GUI;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class DrawCanvas extends Canvas
{
    
    //Images for drawing the board
    //Will need more later. Also double buffereing will have to happen
    private BufferedImage boardBuffer;
    private Graphics2D boardGraphics;
    
    //for placing the battleships on the board. It will temporarily replaces the
    //cursor
    BattleShipPlacement bsp;
    
    //This gathers information on the mouse
    MouseWatcher mouse;
    
    //This organizes information from the mouse
    DrawCanvasMouseMediator med;
    
    public DrawCanvas() throws IOException
    {
        boardBuffer = ImageIO.read(new File("boardImage.png"));
        setSize(407, 333);
        bsp = new BattleShipPlacement();
        
        med = new DrawCanvasMouseMediator();
        mouse = new MouseWatcher(this, med);
    }
    
    /**
     * This method renders everything onto the appropriate buffers and calls
     * paint to put them up on the screen. It is responsible for putting the
     * mapBuffer, windowBuffer, displayBuffer, and cursor on the screen. For your
     * viewing pleasure.
     * @param g 
     */
    @Override
    public void update(Graphics g)
    {
        g.drawImage(boardBuffer, 0, 0, this);
    }
    
    @Override
    public void paint(Graphics g)
    {
        g.drawImage(boardBuffer, 0, 0, this);
    }
}