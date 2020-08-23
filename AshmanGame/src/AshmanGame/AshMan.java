package AshmanGame;

import javafx.animation.AnimationTimer;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import static javafx.scene.paint.Color.RED;
import javafx.scene.paint.Paint;


/**
 *
 * @author Kaylene
 * 03/15/2020
 * If Ash encounters an obstalce, he can't move until the player changes his direction to an open path
 */
public class AshMan extends Character {

    
    public AshMan(int row, int column, int speed, DrawAshMan drawCharacter){
        super(row,column,speed, drawCharacter);
    }
   
   //sets Ash's ability to move to false if current direction has an obstacle in next row/col
   public void encounteredObstacle(boolean[] possibleDirections){
      if(getDirection()>-1){
         if(!possibleDirections[getDirection()]){
            setCanMove(false);
         }else{
            setCanMove(true);
         }
      }
   }
   
 
   
    
}
