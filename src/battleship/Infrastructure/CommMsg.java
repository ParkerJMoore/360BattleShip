/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship.Infrastructure;

import java.io.Serializable;

/**
 *
 * @author parker
 */
public class CommMsg implements Serializable {

    public boolean hit, win;
    public int x;
    public int y;
    
    public CommMsg() {
        hit = win = false;
        x = y = 0;
    }
    
    public CommMsg(int ix, int iy)
    {
        hit = false;
        win = false;
        x = ix;
        y = iy;
    }
    
            
}
