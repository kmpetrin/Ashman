package AshmanGame;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import javafx.scene.media.AudioClip;

/*
 *@author Kaylene
 *08/24/2020
 *Plays url of audio assigned to mResource. Has default game audio but
 *a URL can be passed in to assign a new audio file to mResource and play
 *that sound.
 */
public class GameSounds {
    private URL mResource;
    private int mVolume;
    private AudioClip mSound;
    
    //default game audio sounds
    public GameSounds(){
        mVolume = 1;
        mResource = getClass().getResource("/sounds/intermission.wav");
        mSound = new AudioClip(mResource.toString());
    }
    
    //plays default eat audioclip
    public void playDefaultEat(){
        mResource = getClass().getResource("/sounds/eatcake.wav");
        playWav();
    }
    
    //plays default intermission audioclip
    public void playDefaultIntermission(){
        mResource = getClass().getResource("/sounds/intermission.wav");
        playWav();
    }
    
    //plays default death audioclip
    public void playDefaultDeath(){
        mResource = getClass().getResource("/sounds/death.wav");
        playWav();
    }
    
    //plays the default theme audioclip
    public void playDefaultTheme(){
        mResource = getClass().getResource("/sounds/theme.wav");
        playWav();
    }
    
    //play sound from URL passed in
    public void playSound(URL resource){
        mResource = resource;
        playWav();
    }
      
    //plays the audio
    private void playWav(){
        mSound = new AudioClip(mResource.toString());
        mSound.play(mVolume);
    }
    
}
