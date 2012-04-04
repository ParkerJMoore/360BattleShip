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
        
        
        
        String[] options = new String[] {"Client", "Server", "Cancel"};
        int choice = JOptionPane.showOptionDialog(null, "Server or Client", "Please Choose One:",
        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
        null, options, options[0]);
        
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
        else {
            String input = JOptionPane.showInputDialog(null, "Please Enter IP of Opponent:", "Find Opponent",
            JOptionPane.WARNING_MESSAGE);
            System.out.println(input);
            
            sock =  new Socket(input, 1234); 
        }

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
        MainWindow mw = new MainWindow("Super BattleBoats II", choice, in, out);
        mw.setIconImage(icon);
        mw.setVisible(true);
        
        Painter painter = new Painter(mw.getCanvas());
        painter.start();
    }
}
