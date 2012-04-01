/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

import battleship.GUI.MainWindow;
import battleship.GUI.Painter;
import java.io.IOException;

/**
 *
 * @author Parker
 */
public class BattleShip {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        MainWindow mw = new MainWindow();
        
        Painter painter = new Painter(mw.getCanvas());
        painter.start();
    }
}
