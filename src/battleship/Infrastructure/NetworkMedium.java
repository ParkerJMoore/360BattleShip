/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship.Infrastructure;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author parker
 */
public class NetworkMedium {
    
    private CommMsg msg;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    public NetworkMedium(CommMsg m, ObjectOutputStream o, ObjectInputStream i)
    {
        msg = m;
        out = o;
        in = i;
        
    }
    
    public NetworkMedium()
    {
        msg = new CommMsg();
    }
    
    public void setMove(int x, int y)
    {
        msg = new CommMsg(x,y);
    }
    
    public int getMoveX()
    {
        return msg.getMoveX();
    }
    
    public int getMoveY()
    {
        return msg.getMoveY();
    }
    
    public void setHit(boolean b)
    {
        msg.setHit(b);
    }
    
    public boolean hit()
    {
        return msg.hit();
    }
    
    public void send()
    {
        try {
            out.writeObject(msg);
        } catch (IOException ex) {
            Logger.getLogger(NetworkMedium.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void recieve()
    {
        try {
            msg = (CommMsg)in.readObject();
        } catch (IOException ex) {
            Logger.getLogger(NetworkMedium.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NetworkMedium.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
