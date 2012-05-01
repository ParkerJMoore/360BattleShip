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
import javax.swing.JOptionPane;

/**
 *
 * @author parker
 */
public class GameBoard implements Renderable{

    private BufferedImage boardImage;
    private Graphics2D boardGraphics;
    private int[][] board;
    private int[] shipsLeft;
    private int placed;
    
    public GameBoard(String s) throws IOException
    {
        shipsLeft = new int[5];
        shipsLeft[0] = 3;
        shipsLeft[1] = 2;
        shipsLeft[2] = 3;
        shipsLeft[3] = 4;
        shipsLeft[4] = 5;
        placed = 0;
        
        board = new int[10][10];
        for(int i=0; i<10; i++)
            for(int j=0; j<10; j++)
                board[i][j] = 0;
        
        boardImage = ImageIO.read(new File(s));
        boardGraphics = boardImage.createGraphics();
    }
    
    public boolean placePiece(BufferedImage b, int s, int id, int x, int y)
    {
        boolean placePiece = true;
        for(int i=0; i<s; i++) {
            if(board[x+i][y] != 0) {
                placePiece = false;
            }
        }
        
        if(placePiece == true) {
            for(int i=0; i<s; i++)
                board[x+i][(y)] = id;
            boardGraphics.drawImage(b, x*30, y*30, null);
            return true;
        }
        else {
            JOptionPane.showMessageDialog(null, "Boats are not allowed to overlap.");
            return false;
        }
    }    
    
    public int hit(int x, int y)
    {
        int ret = 0;
        if(board[x][y] != 0) {
            shipsLeft[(board[x][y]-1)]--;
            ret = board[x][y];
            board[x][y] = 0;
        }
        return ret;
    }
    
    public boolean gameOver()
    {
        boolean ret = true;
        for(int i=0; i<10; i++) {
            for(int j=0; j<10; j++) {
                if(board[i][j] != 0) {
                    ret = false;
                }
            }
        }
        return ret;
    }
    
    @Override
    public void render(Graphics2D g, int x, int y) {
        g.drawImage(boardImage, x, y, null);
    }
    
    public int shipsLeft(int i)
    {
        return shipsLeft[i];
    }
    
    public void removeOne(int i)
    {
        shipsLeft[i]--;
    }
}