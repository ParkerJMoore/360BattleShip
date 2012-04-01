package battleship.GUI;

import java.awt.Canvas;

/**
 *
 * @author Sean Kelly, Parker Moore
 */
public class Painter extends Thread
{
    private Canvas canvas;
    public Painter(Canvas toUpdate)
    {
        canvas = toUpdate;
    }
    
    public void run()
    {
        try {
            sleep(500);
        } catch (InterruptedException ex) {}
        while(true)
        {
            canvas.repaint();
            try {
                sleep(15);
            } 
            catch (InterruptedException ex) {
                break;
            }
        }
    }
}
