/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship.Infrastructure;

import java.io.*;
import java.net.URL;
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
    
    public void setMove(int x, int y, int id)
    {
        msg = new CommMsg(x,y,id);
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
    
    public void setWin(boolean b)
    {
        msg.setWin(b);
    }
    
    public boolean win()
    {
        return msg.win();
    }
    
    public String getIP(){
        String ip = "";
        
        try{
            URL url = new URL("http://automation.whatismyip.com/n09230945.asp");
            Object content  = url.getContent();

             if(content instanceof InputStream){
                BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) content));
                ip = br.readLine();
                br.close();
            }
            else{
                ip = content.toString();
            }
        }
        catch(Exception e){

        }
        
        return ip;
    }

    public int getID() {
        return msg.getID();
    }
}
