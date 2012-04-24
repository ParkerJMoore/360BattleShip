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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.imageio.ImageIO;
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

        //get user input and connect to opponent
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        
        
        //Get the users input
        String[] options = new String[] {"Join", "Create", "Cancel"};
        int choice = JOptionPane.showOptionDialog(null, 
                "Join or Create a session", "Please Choose One:",
        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
        null, options, options[0]);
        
        
        //Creat the socket from the given information
        Socket sock =  null;
        ServerSocket servSock = null;
        if(choice == 1)
        {//they chose server
            servSock = new ServerSocket(1234);
            System.out.println("Waiting for your opponent to connect.");
            sock = servSock.accept();
        }
        else if (choice == 0){
            String input = JOptionPane.showInputDialog(null, 
                    "Please Enter IP of desired Opponent:", "Find Opponent",
            JOptionPane.WARNING_MESSAGE);
            System.out.println("Attemption to connect to " + input);
            
            sock =  new Socket(input, 1234); 
            System.out.println("Success! Beginning game.");
        }
        else
            System.exit(1);
        
        
        //Set up the input and output streams
        out = new ObjectOutputStream(sock.getOutputStream());
        in = new ObjectInputStream(sock.getInputStream());
        

        //Make sure the connection was successfull
        CommMsg msg = null;
        if(choice == 1) {
            msg = (CommMsg) in.readObject();
        }
        else {
            System.out.println("Connecting to challenger");
            msg = new CommMsg();
            out.writeObject(msg);
            System.out.println("Success!");
        }
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        //int choice = connectToOpponent(in, out);

        if(out == null)
            System.out.println("out is null in main");
        
        
        //set up the icon for the games window
        BufferedImage icon;
        try{
        icon = ImageIO.read(new FileInputStream(new File("icon.PNG")));
        }catch(Exception e){
            icon = null;
        }
        
        //start up the window
        MainWindow mw = new MainWindow("Super BattleBoats II", choice, in, out);
        mw.setIconImage(icon);
        mw.setVisible(true);
        
        Painter painter = new Painter(mw.getCanvas());
        painter.start();
    }
    
    
    
    private static int connectToOpponent(ObjectInputStream in, ObjectOutputStream out) 
            throws UnknownHostException, IOException, ClassNotFoundException
    {
        
        //Get the users input
        String[] options = new String[] {"Join", "Create", "Cancel"};
        int choice = JOptionPane.showOptionDialog(null, 
                "Join or Create a session", "Please Choose One:",
        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
        null, options, options[0]);
        
        
        //Creat the socket from the given information
        Socket sock =  null;
        ServerSocket servSock = null;
        if(choice == 1)
        {//they chose server
            servSock = new ServerSocket(1234);
            System.out.println("Waiting for your opponent to connect.");
            sock = servSock.accept();
        }
        else if (choice == 0){
            String input = JOptionPane.showInputDialog(null, 
                    "Please Enter IP of desired Opponent:", "Find Opponent",
            JOptionPane.WARNING_MESSAGE);
            System.out.println("Attemption to connect to " + input);
            
            sock =  new Socket(input, 1234); 
            System.out.println("Success! Beginning game.");
        }
        else
            System.exit(1);
        
        
        //Set up the input and output streams
        out = new ObjectOutputStream(sock.getOutputStream());
        in = new ObjectInputStream(sock.getInputStream());
        

        //Make sure the connection was successfull
        CommMsg msg = null;
        if(choice == 1) {
            msg = (CommMsg) in.readObject();
        }
        else {
            System.out.println("Connecting to challenger");
            msg = new CommMsg();
            out.writeObject(msg);
            System.out.println("Success!");
        }
        return choice;
    }
}
