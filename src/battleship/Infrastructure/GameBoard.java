/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship.Infrastructure;

import battleship.GUI.Renderable;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author parker
 */
public class GameBoard implements Renderable{

    private BufferedImage boardImage;
    private Graphics2D boardGraphics;
    private boolean[][] board;
    
    public GameBoard(String s) throws IOException
    {
        board = new boolean[10][10];
        for(int i=0; i<10; i++)
            for(int j=0; j<10; j++)
                board[i][j] = false;
        
        boardImage = ImageIO.read(new File(s));
        boardGraphics = boardImage.createGraphics();
    }
    
    public void placePiece(BufferedImage b, int s, int x, int y)
    {
        System.out.println("X: " + x);
        System.out.println("Y: " + y);
        
        for(int i=0; i<s; i++)
            board[x-1+i][(y-1)] = true;
        boardGraphics.drawImage(b, x*50, y*50, null);
    }    
    
    public boolean hit(int x, int y)
    {
        boolean ret = false;
        if(board[x-1][y-1]==true) {
            board[x-1][y-1] = false;
            ret = true;
        }
        return ret;
    }
    
    @Override
    public void render(Graphics2D g, int x, int y) {
        g.drawImage(boardImage, x, y, null);
    }
}
