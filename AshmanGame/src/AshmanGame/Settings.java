package AshmanGame;
import java.util.prefs.Preferences;
 /*
 *@author Kaylene
 *07/20/2020
 *This class holds all the basic settings of the game.  Canvas size, cell size, speed of characters etc.
 *The maze is designed to be 400 x 400.  It is dividied into 20x20 cells.  Characters move through the maze from cell to cell,
 * by incrementing their x,y positions and then drawing themselves.  Characters start off in the middle of the cell, an increment or decrement of 
 * 20 total x or y coordinates moves the character to the middle of the next cell. The current settings make 4 increments of 5 to move the character from
 * one cell to the next.  When the character is in the middle of the cell, it's col/row position will be updated.
 * User preferences are also kept here, speed of characters, and number of ghosts each level.
 */
public class Settings{
   
   static final int DOWN = 0, UP = 1, LEFT = 2, RIGHT = 3;
   //preferences set by user
   private int initGhosts;
   private int additGhosts;
   
   //settings that can't be changed by the user
   //speed is in milliseconds, so a increments it's positions every X milliseconds, where X is it's speed
   private int mAshSpeed;
   private int mGhostSpeedStart;
   private int mMaxGhostSpeed;
   private int mIncreaseGhostSpeedLvlBy;
   private int mMaxGhostsNum;
   
   //dimensions of maze/canvases
   static final int cellSize = 20;
   static final int ghostInitDist = 5;
   static final double LOGW=400;
   static final double LOGH=400;
   static final double ASPECTRATIO = LOGW/LOGH;
   
   //how many steps to increment the character by 
   static final int incrementBy = 5;
   
   /*number of increments a character has to reach before it is inside a new cell,
   * default setting the character moves 4 times in increments of 5 in order to change cells */
   static final int maxNumIncrements = 20;
   static final int ashStartDirection = -1;
 
   private int mNumLevels;
   private int mCurrentGhostSpeed;
   private int mCurrentGhostNum;
   private int mHighScore;
   
   public Settings(Class read){
      readPreferences(read);
      mAshSpeed = 50;
      mGhostSpeedStart = mAshSpeed*2;
      mMaxGhostSpeed = mAshSpeed/2;
      mIncreaseGhostSpeedLvlBy = 25;
      mMaxGhostsNum = 100;
      mCurrentGhostNum = initGhosts;
      mCurrentGhostSpeed=mGhostSpeedStart;
   }
    
   //gets total levels for the game
   public int getNumLevels(){
      return mNumLevels;
   }
   
   //gets ash's speed
   public int getAshSpeed(){
      return mAshSpeed;
   }
  
  //gets the current ghost speed for the given level
   public int getCurrentGhostSpeed(){
      return mCurrentGhostSpeed;
   }
  
   //gets the current number of ghosts for the given level
   public int getCurrentGhostNum(){
      return mCurrentGhostNum;
   }
   
   //updates ghost speed when level changes
   public void updateGhostSpeed(){
      mCurrentGhostSpeed -= mIncreaseGhostSpeedLvlBy;
      if(mCurrentGhostSpeed < mMaxGhostSpeed){
         mCurrentGhostSpeed = mMaxGhostSpeed;
      }   
   }
  
   //updates number of ghosts when level changes
   public void updateGhostNum(){
      mCurrentGhostNum += additGhosts;
      if(mCurrentGhostNum > mMaxGhostsNum){
         mCurrentGhostNum = mMaxGhostsNum;
      }   
   }
  
   //reads user preferences
   public void readPreferences(Class read){
        Preferences pref = Preferences.userNodeForPackage(read);
        initGhosts = pref.getInt("initGhosts", 2);
        additGhosts = pref.getInt("additGhosts", 2);
        mNumLevels = pref.getInt("mNumLevels", 4);
        mHighScore = pref.getInt("mHighScore", 0);
    }
   
   //stores user preferences   
   public void storePreferences(Class store){
        Preferences pref = Preferences.userNodeForPackage(store);
        pref.putInt("initGhosts", initGhosts);
        pref.putInt("additGhosts", additGhosts);
        pref.putInt("mNumLevels", mNumLevels);
        pref.putInt("mHighScore", mHighScore);
   }
    
   //gets list of preferences 
   public int[] getPreferences(){
      return new int[]{initGhosts, additGhosts, mNumLevels};
   }
   
   //sets the preferences 
   public void setPreferences(int[] newPreferences){    
      initGhosts = newPreferences[0];
      additGhosts = newPreferences[1];
      mNumLevels = newPreferences[2];
   }

   //gets the inital ghosts per level
    public int getInitGhosts(){
        return initGhosts;
    }
    
    //gets the additional ghosts per level
    public int getAdditGhosts(){
        return additGhosts;
    }
    
    //sets the inital ghosts per level
    public void setInitGhosts(int init){
        if(init >-1){
            initGhosts = init;
        }
    }
    
    //sets number of additional ghosts per level
    public void setAdditGhosts(int addit){
        if(addit >-1){
            additGhosts = addit;
        }
    }
    
    //sets number of levels for the game
    public void setNumLevels(int num){
        if(num > 0){
            mNumLevels = num;
        }
    }
    
    //updates user's high score
    public void setHighScore(int num){
        if(num > 0 && num > mHighScore){
            mHighScore = num;
        }
    }
    
    public int getHighScore(){
        return mHighScore;
    }
}
