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
import javax.swing.JFrame;

/**
 *
 * @author Parker Moore, Sean Kelly
 */
public class MainWindow extends JFrame
{ 
    private DrawCanvas canvas;
    
    public MainWindow(String title, int i, ObjectInputStream l, ObjectOutputStream o) throws IOException
    {
        super(title);
        canvas = new DrawCanvas(l, o, i);
        formatComponents();
        //this.setCursor(noCursor);
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
}