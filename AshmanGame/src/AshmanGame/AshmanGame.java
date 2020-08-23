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
    private AshMan ash;
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
    
    @Override
    public void start(Stage primaryStage) {
        Preferences pref = Preferences.userNodeForPackage(getClass());
        Settings.readPreferences(this.getClass());
        mStack = new StackPane();
        mStatus = new Label();

        root = new BorderPane();
        root.setCenter(mStack);
        root.setTop(buildMenuBar());
        mStage = primaryStage;
        mScene = new Scene(root, Settings.LOGH, Settings.LOGW);
        
        ToolBar toolBar = new ToolBar(mStatus);
        root.setBottom(toolBar);
        game = new GamePlay(mScene, mStack, mStatus, goMenuItem,pauseMenuItem);
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
        
        //file menu
        Menu fileMenu = new Menu("_File") ;
        MenuItem quitMenuItem = new MenuItem("_Quit") ;
        quitMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Q,
        KeyCombination.CONTROL_DOWN));
        quitMenuItem.setOnAction(actionEvent -> Platform.exit()) ;
        fileMenu.getItems().addAll(quitMenuItem);
        
        //help menu
        Menu helpMenu = new Menu("_Help") ;
        MenuItem aboutMenuItem = new MenuItem("_About") ;
        aboutMenuItem.setOnAction(actionEvent -> onAbout());
        helpMenu.getItems().add(aboutMenuItem) ;
        
        //game menu
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
        
        menuBar.getMenus().addAll(fileMenu, gameMenu,helpMenu) ;
        return menuBar ; 
    }
    
    
    private void onAbout(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Ashman is a maze arcade game.\nConsume all the cakes and beat all of the levels to win!");
        alert.showAndWait();
    }
    
     /*sets up the preferences, users have the ability to choose how many ghosts
     * are at the start of each level and how many more to add at each next level.
     * These preferences are saved for later gameplay.
     */
     private void onPreferences() {
        game.pauseGame();
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);    
        GridPane grid = new GridPane();
        
        TextField initGhostsEntry = new TextField();
        TextField additGhostsEntry = new TextField();
        
        ArrayList<TextField> entryArray = new ArrayList<TextField>();
        initGhostsEntry.setText(Integer.toString(Settings.initGhosts));
        additGhostsEntry.setText(Integer.toString(Settings.additGhosts));
        Collections.addAll(entryArray, initGhostsEntry, additGhostsEntry);
        
        Text initGhostText = new Text();
        Text additGhostText = new Text();

        initGhostText.setText("Initial ghosts on level 1");
        additGhostText.setText("Additional ghosts per level");

        grid.add(initGhostText,0,0);
        grid.add(additGhostText,0,1);

        grid.add(initGhostsEntry,1,0);
        grid.add(additGhostsEntry,1,1);
           
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Preferences");
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
        dialog.getDialogPane().setContent(grid);
        Optional<ButtonType> result =dialog.showAndWait();
        
        int[] preferences = Settings.getPreferences();
        int[] newPreferences = new int[preferences.length];
        if(result.get()==okButton){
            try{
               int i = 0;
               System.out.println(entryArray.size());
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
               Settings.setPreferences(newPreferences);
               Settings.storePreferences(this.getClass());
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
        game = new GamePlay(mScene, mStack, mStatus, goMenuItem,pauseMenuItem);
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
