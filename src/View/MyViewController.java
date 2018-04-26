package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.*;
import javafx.util.Duration;
import java.io.*;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;



public class MyViewController implements Observer,IView {

    //Data members
    @FXML
    private MyViewModel viewModel;
    private Parent root;
    public void setRoot(Parent root) {
        this.root = root;
    }
    public BorderPane board;
    public MazeDisplayer mazeDisplayer;
    public javafx.scene.control.TextField txt_rowsNum;
    public javafx.scene.control.TextField txt_columnsNum;
    public javafx.scene.control.Button bt_generate;
    public javafx.scene.control.Button bt_solve;
    public javafx.scene.control.TextField txt_status;
    public javafx.stage.Window primaryStage;
    public  MediaPlayer mediaPlayer;
    public boolean win;
    public javafx.scene.control.Button bt_hint;
    public double sx;
    public double sy,tx,ty;
    double characterColumn,characterRow,cellHeight,cellWidth;
    public Stage stage;
    public FXMLLoader fxmlLoader;

    //binding
    public StringProperty CharacterRow = new SimpleStringProperty();
    public StringProperty CharacterColumn = new SimpleStringProperty();


    @Override
    public void update(Observable o, Object arg) {
        if (o.equals(viewModel)) {
            displayMaze(viewModel.getMaze());
            mazeDisplayer.addEventFilter(KeyEvent.KEY_PRESSED,(e)->mazeDisplayer.requestFocus());
        }
    }

    @Override
    public void displayMaze(int[][] maze) {

        int characterPositionRow = viewModel.getCharacterPositionRow();
        int characterPositionColumn = viewModel.getCharacterPositionColumn();
        int goalPositionRow = viewModel.getGoalPositionRowIndex();
        int goalPositionColumn = viewModel.getGoalPositionColumnIndex();
        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
        mazeDisplayer.setGoalPositionColumn(goalPositionColumn);
        mazeDisplayer.setGoalPositionRow(goalPositionRow);
        CharacterRow.set(characterPositionRow + "");
        CharacterColumn.set(characterPositionColumn + "");
        mazeDisplayer.setMaze(maze);
        mazeDisplayer.setOnMouseClicked(e->mousePressed(e));
        mazeDisplayer.setOnMouseReleased(e->mouseRealed(e));

        win=viewModel.win;
        if(win)
        {
            bt_generate.setDisable(false);
            txt_columnsNum.setDisable(false);
            txt_rowsNum.setDisable(false);
            bt_hint.setDisable(true);
            bt_solve.setDisable(true);
            mediaPlayer.stop();
            PlayMovie("resources/winingMovie.mp4","resources/test.mp3",9 );

        }


    }

    //Setters

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setFxmlLoader(FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
    }

    public void setPrimaryStage(Window primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        bt_solve.setDisable(true);
        bt_hint.setDisable(true);
    }



    public void generateMaze() {

           if(txt_rowsNum.getText().equals("")||txt_columnsNum.getText().equals("")) {
               if(txt_rowsNum.getText().equals("")&&txt_columnsNum.getText().equals("")) {
                   Alert alert =
                           new Alert(Alert.AlertType.ERROR,
                                   "please enter the amount of rows and columns in the maze",
                                   ButtonType.OK);
                   alert.setTitle("");
                   Optional<ButtonType> result = alert.showAndWait();
               }
              else if(txt_rowsNum.getText().equals(""))
               {
                   Alert alert =
                           new Alert(Alert.AlertType.ERROR,
                                   "please enter the amount of rows in the maze",
                                   ButtonType.OK);
                   alert.setTitle("");
                   Optional<ButtonType> result = alert.showAndWait();
               }
               else
               {
                   Alert alert =
                           new Alert(Alert.AlertType.ERROR,
                                   "please enter the amount of columns in the maze",
                                   ButtonType.OK);
                   alert.setTitle("");
                   Optional<ButtonType> result = alert.showAndWait();
               }
           }
       else {

               int heigth;
               int width;
               try {
                    heigth = Integer.valueOf(txt_rowsNum.getText());
                    width = Integer.valueOf(txt_columnsNum.getText());
               }catch (Exception e)
               {
                   Alert alert =
                           new Alert(Alert.AlertType.ERROR,
                                   "please enter valid numbers to the rows and column amount",
                                   ButtonType.OK);
                   alert.setTitle("");
                   Optional<ButtonType> result = alert.showAndWait();
                   return;
               }
               PlayMovie ("resources/openingMovie.mp4","resources/gameMusic.mp3",3);
               if(heigth<10)
                   heigth=10;
               if(width<10)
                   width=10;
               bt_generate.setDisable(true);
               bt_solve.setDisable(false);
               bt_hint.setDisable(false);
               txt_columnsNum.setDisable(true);
               txt_rowsNum.setDisable(true);
               viewModel.generateMaze(width, heigth);
           }
    }



    public String getCharacterRow() {
        return CharacterRow.get();
    }
    public StringProperty characterRowProperty() {
        return CharacterRow;
    }
    public String getCharacterColumn() {
        return CharacterColumn.get();
    }
    public StringProperty characterColumnProperty() {
        return CharacterColumn;
    }
    public StringProperty goalRowProperty() {
        return goalRowProperty();
    }
    public StringProperty goalColumnProperty() {return goalColumnProperty();}


    public void setResizeEvent(Scene scene) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                board.setPrefWidth(newSceneWidth.doubleValue()*0.75);
                mazeDisplayer.setWidth(board.getPrefWidth());
               mazeDisplayer.redraw();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                board.setPrefHeight(newSceneHeight.doubleValue()*0.5);
                mazeDisplayer.setHeight(board.getHeight());
                mazeDisplayer.redraw();
            }
        });


    }


    public void Solve() {

        bt_solve.setDisable(true);
        bt_generate.setDisable(false);
        txt_rowsNum.setDisable(false);
        txt_columnsNum.setDisable(false);
        bt_hint.setDisable(true);
        viewModel.Solve(mazeDisplayer.getMaze());
    }

    public void OpenFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("open maze file");
        fc.setInitialDirectory(new File("solutions"));
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("previous mazes", ".txt");
        fc.setSelectedExtensionFilter(extFilter);
        File chosen = fc.showOpenDialog(primaryStage);
        if (chosen != null)
            viewModel.OpenFile(chosen);
    }

    public void Save() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("saveing maze");
        FileChooser.ExtensionFilter extensionFilter= new FileChooser.ExtensionFilter("files type", "*.txt");
        fileChooser.setInitialDirectory(new File("solutions"));
        File retval = fileChooser.showSaveDialog(primaryStage);
        if (retval != null) {
            viewModel.Save(retval);

        }
    }

    public void KeyPressed(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();
    }

    public void BackroundMusic(String path)
    {

        Media sound = new Media(new File(path).toURI().toString());
         this.mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    public void PlayMovie(String path, String pathM,int time)
    {
        if(mediaPlayer!=null)
            mediaPlayer.stop();
        Stage movieStage=new Stage();
        Media sound = new Media(new File(path).toURI().toString());
        MediaPlayer temp=new MediaPlayer(sound);
        MediaView mediaV=new MediaView(temp);
        movieStage.setTitle("Help Mickey & Goofy");
        AnchorPane anchorPane=new AnchorPane();
        Scene scene = new Scene(anchorPane, 1200, 700);
        temp.setAutoPlay(true);
        movieStage.setScene(scene);
        anchorPane.getChildren().add(mediaV);
        movieStage.setResizable(false);
        movieStage.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(time));
        delay.setOnFinished( event -> { BackroundMusic(pathM);
                                        movieStage.close();} );
        delay.play();

    }



    public void Hint()
    {
        viewModel.Hint();


    }

    int dragStartX,dragStartY;
    boolean isCharSelected;
    public void mousePressed(MouseEvent event) {
        mazeDisplayer.addEventFilter(MouseEvent.MOUSE_CLICKED,(e)->mazeDisplayer.requestFocus());
        if(bt_generate.isDisable()&&!(event.isControlDown())) {
            isCharSelected=false;
            sx = event.getY();
            sy = event.getX();
            this.cellWidth = mazeDisplayer.getWidth() / (mazeDisplayer.getMaze().length);
            this.cellHeight = mazeDisplayer.getHeight() / (mazeDisplayer.getMaze()[0].length);
            dragStartX=(int)(sx/this.cellHeight);
            dragStartY=(int)(sy/this.cellWidth);
            isCharSelected = (dragStartX==mazeDisplayer.getCharacterPositionRow()&&dragStartY==mazeDisplayer.getCharacterPositionColumn());

        }
    }

    public void mouseRealed(MouseEvent event) {

        if(bt_generate.isDisable()&&!(event.isControlDown())) {
            tx = event.getY();        //new x target
            ty = event.getX();        //new y target
            this.cellWidth = mazeDisplayer.getWidth() /(mazeDisplayer.getMaze().length);
            this.cellHeight = mazeDisplayer.getHeight() / (mazeDisplayer.getMaze()[0].length);
            int newCharacterRow = (int)(tx/cellHeight);
            int newCharacterCol = (int)(ty/cellWidth);
            if((newCharacterRow!=dragStartX || newCharacterCol!=dragStartY) && isCharSelected)
                if(mazeDisplayer.getMaze()[newCharacterCol][newCharacterRow]!=1)
                    viewModel.MouseP(newCharacterRow,newCharacterCol);
        }
    }

    public void shotAbout() throws Exception{
        Stage stage = new Stage();
        Parent parent = FXMLLoader.load(getClass().getResource("About.fxml"));
        Scene scene = new Scene(parent);
        stage.setTitle("About");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }

    public void showHelp()throws Exception{
        Stage stage = new Stage();
        Parent parent = FXMLLoader.load(getClass().getResource("Help.fxml"));
        Scene scene = new Scene(parent);
        stage.setTitle("Help");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }

    public void showProperties() throws IOException {               //todo add to IView
        PropertiesController properties = new PropertiesController();
        properties.show();
    }

    public void ShutDown()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            viewModel.close();
            stage.close();
        }



    }
}

