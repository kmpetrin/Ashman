package AshmanGame;

import java.io.Serializable;
import javafx.animation.AnimationTimer;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Region;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;


/**
 * @author Kaylene
 * 03/15/2020
 * This class utilizes a canvas to draw the character.  All of the scaling and translating is handled here.
 * Characters have a canvas of 400x400 but are constrained to being drawn in a 20x20 cell.
 * Subclasses must implement their own drawChar(GraphicsContext) method in order for the character
 * to be drawn properly. Direction can be set to indicate which direction eyes, mouth, etc should be facing.
 * The character is drawn on the canvas based on the x, y (or col,row coordinates).
 */
public abstract class DrawCharacter extends Region{
    private  Canvas mCanvas;
    private int mCharDirection, mRowCoord, mColCoord;

    //intiializes the character's speed and position
    public DrawCharacter(int rowCoord, int colCoord){
        mRowCoord = rowCoord;
        mColCoord = colCoord;
        mCanvas = new Canvas(Settings.LOGW*10, Settings.LOGH*10);
        super.setPrefSize(Settings.LOGW*100, Settings.LOGH*100); 
        super.setMinSize(Settings.LOGW/2, Settings.LOGH/2);
        super.getChildren().add(mCanvas);
    }
    
    //Scales the canvas to the window and keeps the aspect ratio
    @Override
    protected void layoutChildren(){
        double width = getWidth();
        double height = getHeight();

        double newHeight = 1/Settings.ASPECTRATIO*width;
        double newWidth= Settings.ASPECTRATIO*height;
        
        if(newWidth>width){
           mCanvas.setWidth(width);
           mCanvas.setHeight(newHeight);
          
        }
        else {
           mCanvas.setWidth(newWidth);
           mCanvas.setHeight(height);
        }
        
        super.layoutInArea(mCanvas, 0, 0, width, height, 0, HPos.CENTER, VPos.CENTER);
        drawChar();
    }
   
    //method that translates and scales the drawing, classes subclass drawChar(gc) to do the actual drawing of character 
    public void drawChar(){
       GraphicsContext gc = mCanvas.getGraphicsContext2D();
       gc.save();
       gc.clearRect(0,0, mCanvas.getWidth(), mCanvas.getHeight());
       double scaleX = mCanvas.getWidth()/Settings.LOGW;
       double scaleY = mCanvas.getHeight()/Settings.LOGH;
       gc.scale(scaleX, scaleY);
       gc.save();
       gc.translate(mColCoord, mRowCoord);
       drawChar(gc);
       gc.restore();
       gc.restore();
       
    }
   
   //actual drawing of character
   abstract void drawChar(GraphicsContext gc);
    
   //gets the rowCoord of drawing or y coordinate
   public int getRowCoord(){
      return mRowCoord;
   }
   
   //gets the colCoord of drawing or x coordinate
   public int getColCoord(){
      return mColCoord;
   }
   
   //sets the rowCoord of drawing or y coordinate
   public void setRowCoord(int rowCoord){
      this.mRowCoord += rowCoord;
   }
   
   //sets the colCoord of drawing or x coordinate
   public void setColCoord(int colCoord){
      this.mColCoord += colCoord;
   }
   
   //sets the characters direction, used for indicating which way the drawing should face
   public void setCharDirection(int direction){
      mCharDirection = direction;
   }
   
   //gets the characters direction
   public int getCharDirection(){
      return mCharDirection;
   }

}
