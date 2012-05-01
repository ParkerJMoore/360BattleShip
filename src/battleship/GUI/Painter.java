package battleship.GUI;

import java.awt.Canvas;

/**
 *
 * @author Sean Kelly, Parker Moore
 */
public class Painter extends Thread
{
    private Canvas canvas;
    private DrawCanvas can;
    public Painter(Canvas toUpdate, DrawCanvas c)
    {
        canvas = toUpdate;
        can = c;
    }
    
    public void run()
    {
        try {
            sleep(500);
        } catch (InterruptedException ex) {}
        while(true)
        {
            can.incTime(10);
            canvas.repaint();
            try {
                sleep(10);
            } 
            catch (InterruptedException ex) {
                break;
            }
        }
    }
}
