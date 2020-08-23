package AshmanGame;

import java.io.Serializable;
import javafx.animation.AnimationTimer;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


/**
 *
 *@author Kaylene
 *03/15/2020
 *This class displays the in game text,as an image, such as pause, win, lose. 
 */
 
public class TextDisplay extends Region{
    private Canvas mCanvas;
    private Image currentImg;
    
    public TextDisplay(Image imgToStart){
        currentImg = imgToStart;
        mCanvas = new Canvas(Settings.LOGW*10, Settings.LOGH*10);
        super.setPrefSize(Settings.LOGW*100, Settings.LOGH*100); 
        super.setMinSize(Settings.LOGW/2, Settings.LOGH/2);
        super.getChildren().add(mCanvas);
    }
    
    @Override
    //Scales the canvas to the window and keeps the aspect ratio
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
        draw();
    }
    
    //draws the current text image 
    public void draw(){
        GraphicsContext gc = mCanvas.getGraphicsContext2D();
        gc.save();
        gc.clearRect(0,0, mCanvas.getWidth(), mCanvas.getHeight());
        double scaleX = mCanvas.getWidth()/Settings.LOGW;
        double scaleY = mCanvas.getHeight()/Settings.LOGH;
        gc.scale(scaleX, scaleY);
        gc.translate(0, 0);
        gc.drawImage(currentImg, 0, Settings.LOGH/4, Settings.LOGH, Settings.LOGW/2);
        gc.restore();
    }
    
    //sets the current image
    public void setImage(Image newImg){
        currentImg = newImg;
    }

    
}
