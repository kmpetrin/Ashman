package AshmanGame;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.prefs.Preferences;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import static AshmanGame.Settings.DOWN;
import static AshmanGame.Settings.LEFT;
import static AshmanGame.Settings.RIGHT;
import static AshmanGame.Settings.UP;

/**
 *
 * @author Kaylene
 * 03/15/2020
 * Revised: 07/20/2020 updated to add settings
 * This is where all gameplay occurs.  It intializes the game and it's characters and maze. 
 * This class handles the player winning, losing, moving to the next level.
 * All character movement is handled here, the characters all run on one timer.
 */
public class GamePlay{
    private ArrayList<Ghost> mGhostArr;
    public  Maze mMaze;
    private AshMan ash;
    private Scene mScene;
    private StackPane mStack;
    private int cakeTotal, score,ashDirection;
    private Label mStatus;
    private TextDisplay text;
    private AnimationTimer gameTimer;
    private Preferences prefs;
    private ArrayList<Character> charArray;
    private boolean playerHasntMoved, isPaused;
    private MenuItem go;
    private MenuItem pause;
    private Settings settings;

    
    public GamePlay(Scene scene, StackPane stack, Label status, MenuItem go, MenuItem pause) {
        settings = new Settings();
        this.go = go;
        this.pause = pause;
        isPaused = false;
        score = 0;
        mScene = scene;
        mStack = stack;
        mStatus = status;
        initializeGame();
        onTheme();
    }
    
    /*intiializes game, creates timer, characters, maze, and adds canvases to stack. 
    * called at each new game or new level
    */
    public void initializeGame(){
        if(settings.getCurrentLevel()==1){
         score=0;
        }
        playerHasntMoved = true;
        gameTimer = new Timer();         
        reset(); 
        mGhostArr = new ArrayList<Ghost>();
        charArray = new ArrayList<Character>();
        mMaze = new Maze();
        cakeTotal = mMaze.getCakesLeft();
        ash = new AshMan(0,0, settings.getAshSpeed(), new DrawAshMan(0,0));
        text = new TextDisplay(GameImages.goImg);
        ashDirection = -1;  
        ash.setDirection(-1); 
        charArray.add(ash);
        mStack.getChildren().addAll(mMaze,ash.getCharacterDrawing());
        initializeGhosts();
        mStack.getChildren().add(text);
        mScene.setOnKeyPressed(key->onKeyPressed(key));
        pauseGame();
        text.draw();
        updateStatus();
    }
    
     //stops time and clears all canvases from stack 
     private void reset() {
        gameTimer.stop();
        mStack.getChildren().clear();
    }  

    //creates a set number of ghosts at the beginning of each level based on the number of ghosts for that level.  
    public void initializeGhosts(){  
        for(int i=0; i<settings.getCurrentGhostNum(); i++){
             int[] coord =positionGenerator();
             Ghost g = new Ghost(coord[0],coord[1], settings.getCurrentGhostSpeed(), new DrawGhost(coord[0]*settings.cellSize,coord[1]*settings.cellSize));
             mGhostArr.add(g);
             charArray.add(g);
             mStack.getChildren().add(g.getCharacterDrawing());
         }
    }
    
    //generates a random position for the ghost to be placed at start of each level
    public int[] positionGenerator(){
        //does not allow ghosts to generator within 5x5 of ash
        Random rand = new Random();
        int x =0;
        int y= settings.cellSize-1;
        while((mMaze.isWall(x,y))){
            x=rand.nextInt(settings.cellSize-settings.ghostInitDist ) + settings.ghostInitDist ;
            y=rand.nextInt(settings.cellSize-settings.ghostInitDist ) + settings.ghostInitDist ;
        }
        int[] coord ={x,y};
        return coord;
    }
    
    /*checks to see if current cell that ash is in has a cake, if so Ash "eats" the cake and sound is played, 
    *status must be updated to reflect score and see if the user has won or can move to the next level
    */
    public void checkCake(){
        if(mMaze.hasCake(ash.getCurrRow(), ash.getCurrCol())){
            mMaze.removeCake(ash.getCurrRow(), ash.getCurrCol());
            cakeTotal = mMaze.getCakesLeft();
            score++;
            onEat();
            updateStatus();
            onCheckWin();
        }
    }
    
    //keeps track of score, cake total, and the level, displayed at bottom of window
    public void updateStatus(){
        mStatus.setText("Score: "+score+"   Cakes: "+cakeTotal+"   Level: "+settings.getCurrentLevel());
    }
    
    //event for user input
    public void onKeyPressed(KeyEvent evt){
        KeyCode code = evt.getCode();
        if((code.equals(KeyCode.DOWN)|| code.equals(KeyCode.UP) || code.equals(KeyCode.LEFT) || code.equals(KeyCode.RIGHT))){
             setAshDirection(code);
        }else if(evt.getCode().equals(KeyCode.HOME)){
            clearCakes();
        }
    }
  
    //stops timer, no characters can move and a pause text is displayed
    public void pauseGame(){
        pause.setDisable(true);
        go.setDisable(false);
        if(!ash.getIsDead() && cakeTotal!=0){
            isPaused=true;
            gameTimer.stop();
            text.setImage(GameImages.goImg);
            text.draw();
       }        
    }
    
    //starts the timer, ghost move and user can move ash so long as ash is not dead  
    public void startGame(){
        go.setDisable(true);
        pause.setDisable(false);
        if(!ash.getIsDead()&& cakeTotal!=0){
            isPaused=false;
            text.setImage(GameImages.clear);
            text.draw();
            gameTimer.start();
        }
    }
         
    public boolean isGamePaused(){
          return isPaused;
      }
           
    //checks to see if ash and ghost are in the same cell, if so the timer stops and user loses the game
    private boolean checkGhostAshCollision() {
        for(Character ghost : mGhostArr){
          if(ghost.getCurrCol() == ash.getCurrCol() && ghost.getCurrRow()==ash.getCurrRow()){
               onDeath();
               gameTimer.stop();
               text.setImage(GameImages.lose);
               text.draw();
               ash.setIsDead(true);
               return true;
           }
        }
        return false;
     }
    
    //clears all cakes in game but one, designed as cheat to debug game
    private void clearCakes() {
        mMaze.clearCakes();
        cakeTotal = mMaze.getCakesLeft();
        updateStatus();
    }
    
    /*checks to see if the player has completed this level
    * if so moves to the next level or displays a "YOU WIN" text, depending on
    * the number of levels left in the game
    */
    private void onCheckWin(){
        if(mMaze.getCakesLeft()==0){
            gameTimer.stop();
            if(settings.getCurrentLevel()!= settings.getNumLevels()){
                settings.setCurrentLevel();
                isPaused = true;
                pauseGame();
                settings.updateGhostNum();
                settings.updateGhostSpeed();
                initializeGame();
                onIntermission();
                
            }else{
               onTheme();
               text.setImage(GameImages.win);
               text.draw();
            }       
        }  
    }
   
    //checks if the next cell, based on the character's current direction, has a wall
    public boolean checkWall(Character character){
       return checkWall(character, character.getDirection());
    }
    
    //checks if the next cell in the given direction variable has a wall or not
    public boolean checkWall(Character character, int direction){
       switch(direction){
         case DOWN:
            return mMaze.isWall(character.getCurrRow()+1,character.getCurrCol());
         case UP:
             return mMaze.isWall(character.getCurrRow()-1,character.getCurrCol());
         case LEFT:
             return mMaze.isWall(character.getCurrRow(),character.getCurrCol()-1);
         case RIGHT:
             return mMaze.isWall(character.getCurrRow(),character.getCurrCol()+1);   
         } 
         return true;
    }
    
     //sets the direction of Ash based on user input
     public void setAshDirection(KeyCode code){   
        if(!ash.getIsDead()){  
        switch(code){      
            case DOWN:
                ashDirection = DOWN;
                break;
            case UP:
                ashDirection = UP;
                break;
            case LEFT:
                ashDirection = LEFT;
                break;
            case RIGHT:
               ashDirection = RIGHT;
               break; 
            }
        }  
        if(playerHasntMoved){
            ash.setDirection(ashDirection);
            playerHasntMoved = false;
        }       
    }
   
   //updates the characters row or col position 
   public void incrementRowOrCol(Character character){
       moveCharacter(character); 
       switch(character.getDirection()){
         case DOWN:
             character.downRow();
             break;
         case UP:
             character.upRow();
             break;
         case LEFT:
             character.leftCol();
             break;
         case RIGHT:
             character.rightCol();
             break;
         }  
    }
   
   //gets an array of possible directions a character can move in
   public boolean[] checkPossibleDirections(Character character){
      boolean[] possibleDirections = new boolean[4];
      int i = 0;
      while(i<4){
         possibleDirections[i]=!checkWall(character, i);
         i++;
      }
      return possibleDirections;
   }
   
   //increments the character's position in the maze by the incrementBy variable
   public void moveCharacter(Character character){
         
         switch(character.getDirection()){
                case DOWN:
                     character.incrementRowCoord();
                     break;
                 case UP:
                     character.decrementRowCoord(); 
                     break;
                 case LEFT:
                     character.decrementColCoord();
                     break;
                 case RIGHT:
                     character.incrementColCoord();
                     break;
          }//switch
   }
   

      /*All characters run on the same timer but have the ability to move at different speed.
      *The character increments, and draws, its self every X milliseconds where X is the speed of the character.
      *When the character has reached it's max number of increments, it's cell position (row and column) are updated. 
      *(Increments start at 0, so a max increment of 20 would only go up to 5: 0, 5, 10, 15. The character's
      *cell position would be update at 15 and the increment count would start again.  The characters draw themselves
      *based on the x, y coords represented by the variables rowCoord(y) and colCoord(x). 
      *So a character moving to the right at increment 0 would draw itsef at rowCoord, colCoord+5,
      *the new colCoord would be colCoord+=5. At increment 5 the character would draw itself at rowCoord,colCoord+5, 
      *the new colCoord would be colCoord+=5, etc)
      *If the character encounters a wall, it handles the encounter based on its subclass type, 
      *current characters cannot move through walls.  If the character cannot move, it makes no increments until it can.
      */
      private class Timer extends AnimationTimer{
         private long lastUpdate = 0;
         private long timeElapsed;
         @Override
         public void handle(long now) {
            now = System.currentTimeMillis();       
            for(Character character: charArray){
               timeElapsed = now-character.getLastUpdated();
               if(timeElapsed >= character.getSpeed() && character.getDirection() > -1){
                  if(!checkWall(character) && character.getCanMove()){
                     if(character.getMoveIncrement()!= Settings.maxNumIncrements-Settings.incrementBy){
                        moveCharacter(character);
                     }else{
                        incrementRowOrCol(character);
                        if(character instanceof AshMan){
                           checkCake();
                           character.setDirection(ashDirection);
                        }
                     }
                     character.drawChar();
                     character.setMoveIncrement();         
                  }else{
                    if(character instanceof AshMan){
                       character.setDirection(ashDirection);
                     } 
                     character.encounteredObstacle(checkPossibleDirections(character)); 
                  }
                  character.setLastUpdated(now); 
                  checkGhostAshCollision();
                  }
             }  
         }
      }
      
      //plays when ash dies
      private void onDeath(){
        URL resource = getClass().getResource("/sounds/death.wav");
        AudioClip sound = new AudioClip(resource.toString());
        sound.play(1);
      }
      
      //plays at beginning of game and when ash wins
      public void onTheme(){
        URL resource = getClass().getResource("/sounds/won.wav");
        AudioClip sound = new AudioClip(resource.toString());
        sound.play(1);
      }
      
      //plays every time ash eats a cake
      private void onEat(){
        URL resource = getClass().getResource("/sounds/eatcake.wav");
        AudioClip sound = new AudioClip(resource.toString());
        sound.play(1);
      }
      
      //plays between level start
      private void onIntermission(){
        URL resource = getClass().getResource("/sounds/intermission.wav");
        AudioClip sound = new AudioClip(resource.toString());
        sound.play(1);
      }
      
}
