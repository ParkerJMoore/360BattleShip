/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship.Infrastructure;

import battleship.GUI.Renderable;
import java.awt.Graphics2D;
import java.io.*;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 *
 * @author Parker
 */
public class BattleShipPlacement implements Renderable {

    private Ship[] ships;
    private int currentShip;
    private int maxSize;
    
    public BattleShipPlacement() throws IOException {
        currentShip = 0;
        ships = new Ship[5];
        readShips();
    }
    
    private void readShips() throws FileNotFoundException, IOException
    {
        BufferedReader br = new BufferedReader(
                new FileReader(new File("ShipList.txt")));

        
        Scanner scan = null;
        scan = new Scanner(br.readLine());
        
        maxSize = scan.nextInt();
        
        for(int i=0; i < maxSize+1; i++) {
            scan = new Scanner(br.readLine());
            ships[i] = new Ship(ImageIO.read(new File(scan.next())), 
                    scan.nextInt(), scan.nextInt(), scan.nextInt(), 
                    scan.nextInt(), scan.nextInt());
        }
    }
    
    public void nextShip()
    {
        currentShip++;
    }
    
    public Ship getPiece()
    {
        return ships[currentShip];
    }
    
    public int getCurrentShipSize()
    {
        return ships[currentShip].getSize();        
    }
    
    
    @Override
    public void render(Graphics2D g, int x, int y) {
        ships[currentShip].render(g, x, y);
    
    }

    public boolean shipsRemaining() {

        boolean retVal = false;
        
        if(currentShip < maxSize)
            retVal = true;
        return retVal;
    }
}
