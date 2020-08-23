
package AshmanGame;

import java.io.Serializable;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;

/**
 *
 * @author Kaylene
 * 03/15/2020
 * Draws the ghost over the particular cell, default cell is 20x20.  
 */
public class DrawGhost extends DrawCharacter{
    private Image ghost;
    
   public DrawGhost(int rowCoord, int colCoord){
       super(rowCoord, colCoord);
        ghost = new Image("images/ghost.png", 100, 100, false, false);
   }
   
   //draws ghost at given coords
    public void drawChar(GraphicsContext gc) {
      gc.drawImage(ghost, 0, 0, Settings.cellSize, Settings.cellSize);
    }
}


