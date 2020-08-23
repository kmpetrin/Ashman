
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
 * @author Kaylene
 * 03/15/2020
 * Ghosts will randomly change direction, to the next available path when they encounter an obstacle.
 */
public class Ghost extends Character{

    public Ghost(int row, int col, int speed, DrawCharacter drawCharacter){
        super(row, col, speed, drawCharacter);
    }

   //randomly selects a new viable direction
   public void encounteredObstacle(boolean[] possibleDirections){      
      int i=0;
      int randDir=Math.abs((new Random().nextInt())%4);
      while(!possibleDirections[randDir]){
         randDir =Math.abs((new Random().nextInt())%4); 
      }
      setDirection(randDir);
   }


   
}


