/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

import battleship.GUI.MainWindow;
import battleship.GUI.Painter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Parker
 */
public class BattleShip {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        //set up the icon for the games window
        BufferedImage icon;
        try{
        icon = ImageIO.read(new FileInputStream(new File("icon.png")));
        }catch(Exception e){
            icon = null;
        }
        
        //start up the window
        MainWindow mw = new MainWindow("BattleBoats");
        mw.setIconImage(icon);
        mw.setVisible(true);
        
        Painter painter = new Painter(mw.getCanvas(), mw.canvas);
        painter.start();
    }
}
