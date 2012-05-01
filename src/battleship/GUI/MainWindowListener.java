/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship.GUI;

import battleship.Infrastructure.CommMsg;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

/**
 *
 * @author parker
 */
public class MainWindowListener implements ActionListener {


    MainWindow mw;
    JFrame gui;
    int choice;
    
    
    public MainWindowListener(MainWindow m)
    {
        mw = m;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        
        if(ae.getActionCommand().equals("Join"))
            setUpClient();
        else if(ae.getActionCommand().equals("Host"))
            setUpServer();
        else if(ae.getActionCommand().equals("Exit")){
            try
            {
              closeGUI();
            }
            catch (Exception e)
            {
              mw.dispose();
            }
        }
    }
    
    private void setUpServer()
    {
        ServerSocket servSock = null;
        Socket sock = null;
        CommMsg msg = null;

        mw.choice = 1;
        
        try {
            servSock = new ServerSocket(1234);
            System.out.println();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Waiting for your opponent to connect.");
        try {
            sock = servSock.accept();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Connected");
        
        try {
            mw.out = new ObjectOutputStream(sock.getOutputStream());
            mw.in = new ObjectInputStream(sock.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(MainWindowListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            msg = (CommMsg) mw.in.readObject();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainWindowListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Opponent has connected");
        
        mw.canvas.setStreams(mw.in, mw.out, mw.choice);
    }
    
    private void setUpClient()
    {
        Socket sock = null;
        CommMsg msg = null;
        mw.choice = 0;
        
        String input = JOptionPane.showInputDialog(null, 
                "Please Enter IP of desired Opponent:", "Find Opponent",
        JOptionPane.WARNING_MESSAGE);

        System.out.println("Attempting to connect to " + input);
        
        try {
            sock =  new Socket(input, 1234);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MainWindowListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            mw.out = new ObjectOutputStream(sock.getOutputStream());
            mw.in = new ObjectInputStream(sock.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(MainWindowListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Success! Beginning game.");
        msg = new CommMsg();
        
        try {
            mw.out.writeObject(msg);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        mw.canvas.setStreams(mw.in, mw.out, mw.choice);
    }

    public ObjectInputStream getInputStream()
    {
        return mw.in;
    }
    
    public ObjectOutputStream getOutputStream()
    {
        return mw.out;
    }
    
    public int getChoice()
    {
        return mw.choice;
    }
    
    public void registerGUI(JFrame g)
    {
        gui = g;
    }
    
    public void closeGUI() throws FileNotFoundException, IOException
    {
        // Shutdown
        gui.dispose();
    }
}
