package battleship.GUI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseWatcher extends MouseAdapter {
        
    DrawCanvasMouseMediator med;
    DrawCanvas canvas;
    boolean canDrag;

    public MouseWatcher(DrawCanvas dc, DrawCanvasMouseMediator m)
    {
        dc.addMouseMotionListener(this);
        dc.addMouseListener(this);
        canvas = dc;
        med = m;
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        med.handleClick(e);
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
    
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
    
    }

    public void mouseMoved(MouseEvent e)
    {
        med.updateCursor(e);
    }
}
