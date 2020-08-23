
package AshmanGame;

import java.io.Serializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;

/**
 *
 *@author Kaylene
 *03/15/2020
 *This class represents the maze in the game.  The walls do no not change as levels go on. 
 *Displays cakes and removes them, keeps track of total cakes and what cakes are in what cell.
 */
public class Maze extends Region{
     static final int WALL = 0, BLANK =1, CAKE = 2;
     private Canvas mCanvas;
     private int mCakesLeft = 247;
     int[][] mMaze={
        {BLANK, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, WALL, WALL, WALL, CAKE, CAKE, CAKE, WALL, CAKE, WALL, WALL},
        {CAKE, WALL, CAKE, WALL, WALL, CAKE, CAKE, WALL, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, WALL, WALL, WALL, CAKE, CAKE, WALL},
        {CAKE, WALL, CAKE, CAKE, WALL, CAKE, CAKE, WALL, CAKE, CAKE, CAKE, WALL, CAKE, CAKE, WALL, CAKE, CAKE, CAKE, WALL, WALL},
        {CAKE, WALL, CAKE, CAKE, WALL, CAKE, WALL, WALL, WALL, CAKE, CAKE, WALL, CAKE, CAKE, CAKE, CAKE, WALL, CAKE, WALL, CAKE},
        {CAKE, WALL, CAKE, CAKE, CAKE, WALL, WALL, CAKE, CAKE, CAKE, CAKE, WALL, CAKE, CAKE, CAKE, CAKE, WALL, CAKE, CAKE, CAKE},
        {CAKE, WALL, WALL, WALL, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, WALL, WALL, WALL, WALL, CAKE, WALL, WALL, WALL, CAKE},
        {CAKE, CAKE, CAKE, WALL, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, WALL, CAKE, CAKE, WALL, CAKE, CAKE},
        {CAKE, WALL, CAKE, WALL, CAKE, CAKE, CAKE, WALL, WALL, WALL, CAKE, WALL, WALL, CAKE, WALL, WALL, CAKE, WALL, CAKE, WALL},
        {CAKE, CAKE, CAKE, WALL, CAKE, CAKE, CAKE, CAKE, WALL, CAKE, CAKE, WALL, CAKE, CAKE, WALL, CAKE, CAKE, WALL, CAKE, WALL},
        {CAKE, WALL, CAKE, WALL, CAKE, WALL, WALL, CAKE, WALL, CAKE, WALL, CAKE, CAKE, CAKE, WALL, WALL, CAKE, WALL, CAKE, WALL},
        {CAKE, WALL, CAKE, CAKE, CAKE, WALL, WALL, CAKE, CAKE, CAKE, WALL, CAKE, WALL, WALL, WALL, CAKE, CAKE, CAKE, CAKE, WALL},
        {CAKE, WALL, CAKE, CAKE, CAKE, CAKE, WALL, CAKE, WALL, WALL, WALL, CAKE, WALL, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE},
        {CAKE, WALL, CAKE, CAKE, CAKE, CAKE, WALL, CAKE, CAKE, CAKE, CAKE, CAKE, WALL, CAKE, WALL, WALL, WALL, CAKE, WALL, CAKE},
        {CAKE, WALL, WALL, WALL, CAKE, CAKE, WALL, CAKE, WALL, WALL, CAKE, CAKE, CAKE, CAKE, WALL, CAKE, WALL, CAKE, WALL, CAKE},
        {CAKE, WALL, CAKE, CAKE, CAKE, WALL, WALL, CAKE, CAKE, WALL, CAKE, WALL, WALL, WALL, WALL, CAKE, WALL, CAKE, WALL, CAKE},
        {CAKE, WALL, WALL, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, WALL, CAKE, WALL, WALL},
        {CAKE, CAKE, CAKE, CAKE, CAKE, WALL, WALL, WALL, WALL, CAKE, WALL, WALL, WALL, WALL, WALL, CAKE, WALL, CAKE, CAKE, WALL},
        {WALL, CAKE, WALL, CAKE, CAKE, CAKE, CAKE, CAKE, WALL, CAKE, CAKE, CAKE, CAKE, CAKE, WALL, CAKE, CAKE, CAKE, CAKE, WALL},
        {WALL, CAKE, WALL, CAKE, WALL, CAKE, WALL, CAKE, WALL, CAKE, WALL, WALL, WALL, CAKE, WALL, CAKE, WALL, WALL, CAKE, CAKE},
        {WALL, WALL, WALL, CAKE, WALL, CAKE, WALL, CAKE, WALL, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE, CAKE}
     
        // 0     1       2    3    4    5      6     7    8     9     10     11   12    13    14    15    16    17   18    19
     };
     //each "cell" is 20x20
     public void setMaze(int[][] maze){
         mMaze=maze;
         
     }
     public Maze(){
        mCanvas = new Canvas(Settings.LOGW, Settings.LOGH);
        super.getChildren().add(mCanvas);
        super.setPrefSize(Settings.LOGW*10, Settings.LOGH*10); 
        super.setMinSize(Settings.LOGW/10, Settings.LOGH/10);
        createMaze();
     }
     
   //creates the maze by iterating over maze array and calling the draw method to visualize the maze
     public void createMaze() {
       GraphicsContext gc = mCanvas.getGraphicsContext2D();
       gc.save();
       gc.clearRect(0,0, mCanvas.getWidth(), mCanvas.getHeight());
       double scaleX = mCanvas.getWidth()/Settings.LOGW;
       double scaleY = mCanvas.getHeight()/Settings.LOGH;
       gc.setFill(Paint.valueOf("BLUE"));
       gc.fillRect(0,0, mCanvas.getWidth(), mCanvas.getHeight());
       gc.scale(scaleX, scaleY);
        for(int rows = 0; rows < 20; rows++){
            for(int columns = 0; columns < 20; columns++){
                draw(rows, columns, gc);
            }
        }
        gc.restore();
    }
    
    //draws maze and cakes, helper method for creating the maze 
     private void draw(int row, int column, GraphicsContext gc){
         switch(mMaze[row][column]){
             case WALL:
                 gc.save();
                 gc.translate(column*Settings.cellSize, row*Settings.cellSize);
                 gc.setFill(Paint.valueOf("BLACK"));
                 gc.fillRect(0, 0, Settings.cellSize, Settings.cellSize);
                 gc.restore();
                 break;
             case CAKE:
                 gc.save();
                 gc.translate(column*Settings.cellSize, row*Settings.cellSize);
                 gc.setFill(Paint.valueOf("WHITE"));
                 gc.fillOval(.4*Settings.cellSize, .4*Settings.cellSize, 0.2*Settings.cellSize, 0.2*Settings.cellSize);
                 gc.restore();
                 break;
         }       
      }
     
     
     public int[][] getMaze(){
         return this.mMaze;
     }
     
     //tells if passed row and column is a wall or not
     public boolean isWall(int row, int column){
         if(row > 19 || row < 0 || column < 0 || column > 19) return true;
         return mMaze[row][column] == Maze.WALL;
     }
    
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
        createMaze(); 
    }
    
    //removes the cake and redraws the cell as empty
    public void removeCake(int row, int column){
        mCakesLeft-=1;
        drawRemovedCake(row,column);
        mMaze[row][column]=BLANK;
    }
    
    //returns if the given cell has a cake or not
    public boolean hasCake(int row, int column){
         return mMaze[row][column] == Maze.CAKE;
    }
    
    //draws a blue cell at the passed position
    public void drawRemovedCake(int row, int column){
       GraphicsContext gc = mCanvas.getGraphicsContext2D();
       gc.save();
       double scaleX = mCanvas.getWidth()/Settings.LOGW;
       double scaleY = mCanvas.getHeight()/Settings.LOGH;
       gc.scale(scaleX, scaleY);
       gc.translate(column*Settings.cellSize, row*Settings.cellSize);
       gc.setFill(Paint.valueOf("BLUE"));
       gc.fillRect(0, 0, Settings.cellSize, Settings.cellSize);
       gc.restore();
    }
    
    //gets total number of cakes consumed by Ashman
    public int getCakesLeft(){
        return this.mCakesLeft;
    }
    
    //gets rid of all cakes except 1, this was designed as a cheat for debugging the game
    public void clearCakes() {
        int[][] clearMaze = new int[20][20];
        for(int row = 0; row < 20; row++){
            for(int column = 0; column < 20; column++){
                switch(mMaze[row][column]){
                    case WALL:
                        clearMaze[row][column] = WALL;
                        break;
                    default:
                        clearMaze[row][column] = BLANK;
                        break;
                }
            }
        }
        clearMaze[0][6] = CAKE;
        mMaze = clearMaze;
        createMaze();
        mCakesLeft = 1;
    }
    
    
 
}
