package battleship.GUI;

import java.awt.event.MouseEvent;

/**
 *
 * @author Parker
 */
public class DrawCanvasMouseMediator {
   
    private int currX, currY;
    
    public DrawCanvasMouseMediator()
    {
        currX = currY = 0;
    }

    public void updateCursor(MouseEvent e) {
        currX = e.getX();
        currY = e.getY();
    }
    
    public int getMouseX()
    {
        return currX;
    }
    
    public int getMouseY()
    {
        return currY;
    }
}
