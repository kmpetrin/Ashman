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
import static AshmanGame.Settings.DOWN;
import static AshmanGame.Settings.LEFT;
import static AshmanGame.Settings.RIGHT;
import static AshmanGame.Settings.UP;




/**
 * @author Kaylene
 * 03/15/2020
 * Draws ash man. His mouth opens every other increment.  Direction is used to decide which way his mouth should be facing.
 */
public class DrawAshMan extends DrawCharacter {
    private int cell = Settings.cellSize;
    private double[] closed ={0.05*cell, 0.05*cell, 0.9*cell, 0.9*cell};
    private double[] mouthX={cell,cell/2,cell};
    private double[] mouthXUp={0,cell/2,cell};
    private double[] mouthY={0,cell/2,cell};
    private double[] mouthYDown={cell,cell/2,20};
    private double[] mouthYUp={0,cell/2,0};
    private double[] mouthXLeft={0,cell/2,0};
    private int mMouthOpen;
   
    public DrawAshMan(int rowCoord, int colCoord){
       super(rowCoord, colCoord);
       mMouthOpen = 0;
   }
   
   //draws a yellow circle and overlays a blue triangle if Ash's mouth is supposed ot be open
   public void drawChar(GraphicsContext gc){
       mMouthOpen =(mMouthOpen+1)%2;
       gc.setFill(Paint.valueOf("YELLOW"));
       gc.fillOval(closed[0],closed[1],closed[2],closed[3]);
       if(mMouthOpen == 1){//mouth opens every other move
         mouthDirection(gc);
       }
   }
      
    //draws mouth depending on direction of Ash
    private void mouthDirection(GraphicsContext gc){
        if(getCharDirection() == DOWN){
            gc.setFill(Paint.valueOf("BLUE"));
            gc.fillPolygon(mouthXUp,mouthYDown,3);
        }else if(getCharDirection() == UP){
            gc.setFill(Paint.valueOf("BLUE"));
            gc.fillPolygon(mouthXUp,mouthYUp,3);   
        }else if(getCharDirection() == RIGHT){
            gc.setFill(Paint.valueOf("BLUE"));
            gc.fillPolygon(mouthX,mouthY,3);            
        }else if(getCharDirection()== LEFT){
            gc.setFill(Paint.valueOf("BLUE"));
           gc.fillPolygon(mouthXLeft,mouthY,3);
        }
    }
   
}
