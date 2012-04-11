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

    private boolean hit, win;
    private int moveX;
    private int moveY;
    private int mouseX;
    private int mouseY;
    private boolean hasGone;
    
    public CommMsg() {
        hit = win = hasGone = false;
        moveX = moveY = mouseX = mouseY =  0;
    }
    
    public CommMsg(int ix, int iy)
    {
        hit = false;
        win = false;
        hasGone = false;
        moveX = ix;
        moveY = iy;
    }
    
    public int getMoveX() {
        return moveX;
    }
    
    public int getMoveY() {
        return moveY;
    }
    
    public int getMouseX() {
        return mouseX;
    }
    
    public int getMouseY() {
        return moveY;
    }
    
    public void setMove(int x, int y)
    {
        moveX = x;
        moveY = y;
    }
    
    public void setMouse(int x, int y)
    {
        mouseX = x;
        mouseY = y;
    }
    
    public boolean hasGone()
    {
        return hasGone;
    }
    
    public void setTurnStatus(boolean b)
    {
        hasGone = b;
    }
    
    public void setHit(boolean b)
    {
        hit = b;
    }
    
    public boolean hit()
    {
        return hit;
    }
}
