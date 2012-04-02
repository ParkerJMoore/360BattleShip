/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

import battleship.GUI.MainWindow;
import battleship.GUI.Painter;
import battleship.Infrastructure.CommMsg;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Parker
 */
public class BattleShip {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        //get user input
        JFrame parent = new JFrame();
        JOptionPane optionPane = new JOptionPane("Would you like to be the client?", 
                JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
        JDialog dialog = optionPane.createDialog(parent, "CHOOSE ONE!!!");
        dialog.setVisible(true);
    
        int choice = (Integer)optionPane.getValue();
        InetAddress servAddr = InetAddress.getLocalHost();

        Socket sock =  null;
        ServerSocket servSock = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        
        
        if(choice == 1)
        {//they chose server
            servSock = new ServerSocket(1234);
            sock = servSock.accept();
            System.out.println("Waiting for your opponent to connect.");
        }
        else
            sock =  new Socket(servAddr, 1234); 

        out = new ObjectOutputStream(sock.getOutputStream());
        in = new ObjectInputStream(sock.getInputStream());
        
        CommMsg msg = null;
        
        if(choice == 1) {
            msg = (CommMsg) in.readObject();
        }
        else {
            System.out.println("Connecting to selected opponent");
            msg = new CommMsg();
            out.writeObject(msg);
            System.out.println("Success!");
        }

        BufferedImage icon;
        try{
        icon = ImageIO.read(new FileInputStream(new File("icon.PNG")));
        }catch(Exception e){
            icon = null;
        }
        MainWindow mw = new MainWindow("Super BattleBoats II", out, in, choice);
        mw.setIconImage(icon);
        mw.setVisible(true);
        
        Painter painter = new Painter(mw.getCanvas());
        painter.start();
    }
}
