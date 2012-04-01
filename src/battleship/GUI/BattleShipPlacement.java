/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship.GUI;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 *
 * @author Parker
 */
public class BattleShipPlacement implements Renderable {

    private BufferedImage[] ships;
    private int currentShip;
    
    public BattleShipPlacement() throws IOException {
        currentShip = 0;
        readShips();
    }
    
    private void readShips() throws FileNotFoundException, IOException
    {
        /*BufferedReader br = new BufferedReader(
                new FileReader(new File("ShipList.txt")));
        
        String s = br.readLine();
        Scanner scan = new Scanner(s);
        s = scan.next();
        
        for(int i=0; i < 1; i++) {
            System.out.println(s);
            ships[0] = ImageIO.read(new File(s));
        }*/
    }
    
    public void nextShip()
    {
        currentShip++;
    }
    
    
    @Override
    public void render(Graphics2D g, int x, int y) {
        g.drawImage(ships[currentShip], x, y, null);
    
    }
    
}
