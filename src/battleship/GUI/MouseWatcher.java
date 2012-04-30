package battleship.GUI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        try {
            med.handleClick(e);
        } catch (IOException ex) {
            Logger.getLogger(MouseWatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MouseWatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mouseMoved(MouseEvent e)
    {
        med.updateCursor(e);
    }
}
