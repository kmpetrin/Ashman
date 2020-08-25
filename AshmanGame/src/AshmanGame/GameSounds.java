package AshmanGame;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

/*
 *@author Kaylene
 *08/24/2020
 *Contains the in game audio, default audio clips are in the constructor
 *audio can be changed
 */
public class GameSounds {
    private URL mIntermission;
    private URL mEatCake;
    private URL mDeath;
    private URL mTheme;
    
    //default game audio sounds
    public GameSounds(){
        mIntermission= getClass().getResource("/sounds/intermission.wav");
        mEatCake = getClass().getResource("/sounds/eatcake.wav");
        mDeath = getClass().getResource("/sounds/death.wav");
        mTheme = getClass().getResource("/sounds/theme.wav");
    }
    
    //gets the audio played between level changes
    public URL getOnIntermission(){
        return mIntermission;
    }
    
    //gets the audio played when ash eats a cake
    public URL getOnEatCake(){
        return mEatCake;
    }
    
    //gets the audio played on ash's death
    public URL getOnDeath(){
        return mDeath;
    }
    
    //gets the audio played at beginning of game and on win
    public URL getOnTheme(){
        return mTheme;
    }
   
    //Sets the audio played after each level is complete
    public void setOnIntermission(URL path){
        mIntermission = path;
    }
    
    //Sets the audio played when ash eats a cake
    public void setOnEatCake(URL path){
        mEatCake = path;
    }
    
     //Sets the audio played on ash's death
    public void setOnDeath(URL path){
        mDeath = path;
    }
    
    //Sets the audio played at the beginning of the game and on win
    public void setOnTheme(URL path){
        mTheme = path;
    }
    
}
