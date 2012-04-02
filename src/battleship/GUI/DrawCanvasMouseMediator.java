package battleship.GUI;

import java.awt.event.MouseEvent;

/**
 *
 * @author Parker
 */
public class DrawCanvasMouseMediator {
   
    private int currX, currY;
    private DrawCanvas canvas;
    
    public DrawCanvasMouseMediator(DrawCanvas c)
    {
        currX = currY = 0;
        canvas = c;
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

    void handleClick(MouseEvent e) {
        canvas.reactToClick();
    }
}
