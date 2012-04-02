/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship.Infrastructure;

import battleship.GUI.Renderable;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 *
 * @author parker
 */
public class Ship implements Renderable {

    private int x, y, w, h, size;
    BufferedImage shipImage;
    
    
    public Ship(BufferedImage b, int ix, int iy, int iw, int ih, int s) {
        x = ix;
        y = iy;
        w = iw;
        h = ih;
        size = s;
        shipImage = b.getSubimage(x, y, w, h);
    }
    
    public BufferedImage getImage()
    {
        return shipImage;
    }
    
    public int getSize()
    {
        return size;
    }
    
    @Override
    public void render(Graphics2D g, int x, int y) {
        g.drawImage(shipImage, x, y, null);
    
    }
    
}
