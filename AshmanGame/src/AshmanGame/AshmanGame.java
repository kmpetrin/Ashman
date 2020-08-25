package AshmanGame;

import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 *
 * @author Kaylene
 * 03/15/2020
 * This class contains the main method, it builds the menu bar, sets up the size of the window, and 
 * sets up the basic buttons
 */
public class AshmanGame extends Application{
    private File mFile;
    private Stage mStage;
    private Maze mMaze;
    private Scene mScene;
    private int numGhosts;
    private Ghost[] ghostArr;
    private StackPane mStack;
    private ArrayList<Character> mCharactersArr;
    private boolean isPaused;
    private Label mStatus;
    private GamePlay game;
    private MenuItem goMenuItem = new MenuItem("Go") ;
    private MenuItem pauseMenuItem = new MenuItem("Pause") ;
    private BorderPane root;
    private Settings settings;
    private ButtonType mOkButton,mCancelButton;
    private GridPane mGrid;
    private TextField mInitGhostsEntry, mAdditGhostsEntry, mNumLevelsEntry;
    private Dialog<ButtonType> mDialog;
    private Optional<ButtonType> mResult;
    
    @Override
    public void start(Stage primaryStage) {
        settings = new Settings(this.getClass());
        mStack = new StackPane();
        mStatus = new Label();

        root = new BorderPane();
        root.setCenter(mStack);
        root.setTop(buildMenuBar());
        mStage = primaryStage;
        mScene = new Scene(root, Settings.LOGH, Settings.LOGW);
        
        ToolBar toolBar = new ToolBar(mStatus);
        root.setBottom(toolBar);
        onNew();
        primaryStage.setTitle("AshMan");
        primaryStage.setScene(mScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
    
   //builds the menu bar. Game has pause, go, new, quit, settings, and about button options.
    public MenuBar buildMenuBar(){
        MenuBar menuBar = new MenuBar() ;
        menuBar.getMenus().addAll(fileMenu(), gameMenu(), helpMenu()) ;
        return menuBar ; 
    }
    
    //creates fileMenu and returns it
    private Menu fileMenu(){
        Menu fileMenu = new Menu("_File") ;
        MenuItem quitMenuItem = new MenuItem("_Quit") ;
        quitMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Q,
        KeyCombination.CONTROL_DOWN));
        quitMenuItem.setOnAction(actionEvent -> Platform.exit()) ;
        fileMenu.getItems().addAll(quitMenuItem);
        return fileMenu;
    }
    
    //creates helpMenu and returns it
    private Menu helpMenu(){
        Menu helpMenu = new Menu("_Help") ;
        MenuItem aboutMenuItem = new MenuItem("_About") ;
        aboutMenuItem.setOnAction(actionEvent -> onAbout());
        helpMenu.getItems().add(aboutMenuItem);
        return helpMenu;
    }
    
    //creates gameMenu and returns it
    private Menu gameMenu(){
        Menu gameMenu = new Menu("_Game") ;
        MenuItem  newMenuItem = new MenuItem("New") ;
        MenuItem settingsMenuItem = new MenuItem("Settings") ;
        settingsMenuItem.setOnAction(actionEvent -> onPreferences());
        newMenuItem.setOnAction(actionEvent -> onNew());
        goMenuItem.setOnAction(actionEvent -> onGo());
        pauseMenuItem.setOnAction(actionEvent -> onPause());
        gameMenu.getItems().addAll(newMenuItem,new SeparatorMenuItem(),goMenuItem,pauseMenuItem,new SeparatorMenuItem(),settingsMenuItem);
        newMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.N,KeyCombination.CONTROL_DOWN));
        goMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.G));
        pauseMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.P));
        settingsMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S,KeyCombination.CONTROL_DOWN));
        buildPreferencesDialog();
        return gameMenu;
    }
    
    private void onAbout(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Ashman is a maze arcade game.\nConsume all the cakes and beat all of the levels to win!");
        alert.showAndWait();
    }
    
    /*partially builds the preferences dialog box
    *intializes buttons and grid pane, along with text and text field
    *for settings
    */
    private void buildPreferencesDialog(){
        mOkButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        mCancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE); 
        mGrid = new GridPane();
        
        mGrid.add(new Text("Initial ghosts on level 1"),0,0);
        mGrid.add(new Text("Additional ghosts per level"),0,1);
        mGrid.add(new Text("Number of Levels (must reset game)"),0,2);
        
        mInitGhostsEntry = new TextField();
        mAdditGhostsEntry = new TextField();
        mNumLevelsEntry = new TextField(); 
        
        
        mGrid.add(mInitGhostsEntry,1,0);
        mGrid.add(mAdditGhostsEntry,1,1);
        mGrid.add(mNumLevelsEntry,1,2);    
    }

    //reads the user preferences 
    private void readPreferencesFromSettings(){
        mInitGhostsEntry.setText(Integer.toString(settings.getInitGhosts()));
        mAdditGhostsEntry.setText(Integer.toString(settings.getAdditGhosts()));
        mNumLevelsEntry.setText(Integer.toString(settings.getNumLevels()));
    }
    
    /* dialog boxe pops up and pauses game allowing user to modify settings
    * stores preferences if user enters valid input
    */
     private void onPreferences() {
        game.pauseGame();
        readPreferencesFromSettings();
        mDialog = new Dialog<>();
        mDialog.setTitle("Preferences");
        mDialog.getDialogPane().getButtonTypes().addAll(mOkButton, mCancelButton);
        mDialog.getDialogPane().setContent(mGrid);
        mResult = mDialog.showAndWait();    
        
        ArrayList<TextField> entryArray = new ArrayList<TextField>();
        Collections.addAll(entryArray, mInitGhostsEntry, mAdditGhostsEntry, mNumLevelsEntry);
        
        int[] preferences = settings.getPreferences();
        int[] newPreferences = new int[preferences.length];
        if(mResult.get()==mOkButton){
            try{
               int i = 0;
               for(TextField text : entryArray){
                  int temp;
                  if(text.getText().equals("")){
                     temp = preferences[i];
                  }else{
                     temp = Integer.parseInt(text.getText());
                  }if(temp < 0){
                     onIntError();
                     return;
                  }
                   newPreferences[i]=temp;
                   i++;
               }
               settings.setPreferences(newPreferences);
               settings.storePreferences(this.getClass());
               }catch(Exception e){
                onIntError();
            }    
        }        
    }
     
     //error alter if user does not enter an int for ghost number in preferences
     private void onIntError(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("You must enter an integer");
        alert.showAndWait();
   }
     
    //starts a new game
    public void onNew(){
        settings = new Settings(this.getClass());
        game = new GamePlay(mScene, mStack, mStatus, goMenuItem,pauseMenuItem, settings);
    }

   //starts game
    private void onGo() {
       game.startGame();
       goMenuItem.setDisable(true);
       pauseMenuItem.setDisable(false);
    }

   //pauses game
    private void onPause() {
        game.pauseGame();
        pauseMenuItem.setDisable(true);
        goMenuItem.setDisable(false);
    }
 }
