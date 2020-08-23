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
 *
 * @author Kaylene
 * 03/15/2020
 * The character class holds the characters speed, ability to move, and when it was last updated.
 * It also tells whether the character is dead or alive.  The character has the ability to draw itself
 * when asked.  The character moves from cell to cell (or row to row/col to col) in increments. The character
 * must make the max number of increments before it is considered inside it's new cell 
 */
public abstract class Character{
    private long mLastUpdated;
    private int mCurrentRow, mCurrentCol,mSpeed, mDirection, mMoveIncrement;
    private boolean mIsDead, mCanMove;
    private DrawCharacter mDrawCharacter;
    
    //intiializes the character's speed and position
    public Character (int startRow, int startCol, int speed, DrawCharacter drawCharacter){
        mSpeed = speed;
        mCurrentRow = startRow;
        mCurrentCol = startCol;
        mMoveIncrement = 0;
        mIsDead = false;
        mCanMove = true;
        mDrawCharacter = drawCharacter;
    }
    
   //returns if character is dead of not 
   public boolean getIsDead(){
        return mIsDead;
    }
   
   //sets the character to dead or alive 
   public void setIsDead(boolean dead){
        mIsDead = dead;
   }
   
   //different characters handle obstacles they've encountered differently
   public abstract void encounteredObstacle(boolean[] possibleDirections);
   
   //draws the character at it's current position
   public void drawChar(){
      mDrawCharacter.drawChar();
   }
   
   //increments the characters row position
   public int downRow(){      
       return mCurrentRow+=1;
    }
   
    //decrements the characters row position
    public int upRow(){
        return mCurrentRow-=1;
    }
    
    //decrements the characters col position
    public int leftCol(){
        return mCurrentCol-=1;
    }
    
    //increments the characters col position
    public int rightCol(){
        return mCurrentCol+=1;
    }
    
    //returns character drawing so it may be added to the stack
    public DrawCharacter getCharacterDrawing(){
      return mDrawCharacter;
    }
    
    /*The character moves from row to row and col to col in increments. The number of increments is determined
     *by the cell size divided by the desire maxNumIncrements.  A higher maxNumIncrements can increase the smoothness
     * of transitioning from cell to cell.  The character makes the max number of increments to the next cell, and then
     * it's row or col position is updated 
     */ 
    public void setMoveIncrement(){
      mMoveIncrement=(mMoveIncrement+Settings.incrementBy)%Settings.maxNumIncrements;
    }
    
    //returns the current move increment of the character
    public int getMoveIncrement(){
      return mMoveIncrement;
    }   
    
    //returns the current row the character is in
    public int getCurrRow(){
        return mCurrentRow;
    }
    
    //returns the current col the character is in
     public int getCurrCol(){
        return mCurrentCol;
    }
    
    //gets the speed of the character 
    public int getSpeed(){
        return mSpeed;
    }
    
    //gets the current direction the character is headed in
    public int getDirection(){
        return mDirection;
    }
    
    //sets the direction the character will head in
    public void setDirection(int direction){
        mDirection = direction;
        mDrawCharacter.setCharDirection(mDirection);
    }
   
   //gets the last time the character moved
   public long getLastUpdated(){
      return mLastUpdated;
   }
   
   //sets the last time the charactr has moved
   public void setLastUpdated(long newTime){
      mLastUpdated = newTime;
   }
   
   //gets the actual y position of the character drawing
   public int getRowCoord(){
      return mDrawCharacter.getRowCoord();
   }
   
   //gets the actual x position of the character drawing
   public int getColCoord(){
      return mDrawCharacter.getColCoord();
   }
   
   //moves the character to the right by the incremented value
   public void incrementColCoord(){
      mDrawCharacter.setColCoord(Settings.incrementBy);
   }
   
   //moves the character to the left by the incremented value
   public void decrementColCoord(){
      mDrawCharacter.setColCoord(-Settings.incrementBy);
   }
   
   //moves the character down by the incremented value
    public void incrementRowCoord(){
      mDrawCharacter.setRowCoord(Settings.incrementBy);
   }
   
   //moves the character up by the incremented value
   public void decrementRowCoord(){
      mDrawCharacter.setRowCoord(-Settings.incrementBy);
   }

   //returns if the character can currently move or not
   public boolean getCanMove(){
      return mCanMove;
   }
   
   //sets if the character can currently move or not
   public void setCanMove(boolean canMove){
      mCanMove = canMove;
   }
   
}
