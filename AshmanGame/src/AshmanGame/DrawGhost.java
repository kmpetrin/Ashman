
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
    private Image mGhost;
    
   public DrawGhost(int rowCoord, int colCoord){
       super(rowCoord, colCoord);
       setGhostImage(GameImages.ghost);
   }
   
   //draws ghost at given coords
    public void drawChar(GraphicsContext gc) {
      gc.drawImage(mGhost, 0, 0, Settings.cellSize, Settings.cellSize);
    }
    
    public void setGhostImage(String path){
        mGhost = new Image(path, Settings.LOGW, Settings.LOGH, false, true);
    }
}


