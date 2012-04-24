/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package battleship.GUI;

import java.awt.Canvas;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.*;

/**
 *
 * @author Parker Moore, Sean Kelly
 */
public class MainWindow extends JFrame
{ 
    public DrawCanvas canvas;
    private MainWindowListener mwl;
    public  ObjectInputStream in;
    public  ObjectOutputStream out;
    public  int choice;
    
    public MainWindow(String title) throws IOException
    {
        super(title);

        //add the listener for the menu buttons
        mwl = new MainWindowListener(this);
        
        //create a menu bar and add a listener
        createMenuItems();
        
        //add the canvas
        canvas = new DrawCanvas(mwl);
        formatComponents();
    }
    
    private final BufferedImage noCursorImg = 
            new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    private final java.awt.Cursor noCursor = Toolkit.getDefaultToolkit()
            .createCustomCursor(noCursorImg, new Point(0, 0), "no cursor");
    
    /* Window Components */
    private void formatComponents()
    {
        setVisible(true);
        setResizable(false);

        // Format frame components
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(canvas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(canvas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        // TODO fix resolution to come from properties file

    }
    
    public Canvas getCanvas()
    {
        return canvas;
    }

    private void createMenuItems() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        JMenu fileMenu = new JMenu("Match");
        menuBar.add(fileMenu);
        
        JMenuItem clientAction = new JMenuItem("Join");
        JMenuItem serverAction = new JMenuItem("Create");
        
        fileMenu.add(clientAction);
        fileMenu.add(serverAction);
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(clientAction);
        bg.add(serverAction);
        
        //add the listener for the buttons
        clientAction.addActionListener(mwl);
        serverAction.addActionListener(mwl);
    }
}