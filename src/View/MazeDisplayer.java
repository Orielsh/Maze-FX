package View;



/**
 * Created by elias_000 on 21/06/2017.
 */
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.io.FileNotFoundException;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

    /**
     * Created by Aviadjo on 3/9/2017.
     */
    public class MazeDisplayer extends Canvas {

        private int[][] maze;
        private int characterPositionRow = 1;
        private int characterPositionColumn = 1;
        private int goalPositionRow;
        private int goalPositionColumn;

        public MazeDisplayer() {
            widthProperty().addListener(evt -> redraw());
            heightProperty().addListener(evt -> redraw());
        }


        public void setMaze(int[][] maze) {
            this.maze = maze;
            redraw();
        }

        public void setCharacterPosition(int row, int column) {
            characterPositionRow = row;
            characterPositionColumn = column;
            redraw();
        }

        public int getCharacterPositionRow() {
            return characterPositionRow;
        }

        public int getCharacterPositionColumn() {
            return characterPositionColumn;
        }

        public int[][] getMaze() {
            return maze;
        }



        public void redraw() {
            if (maze != null) {
                // double canvasHeight = getHeight();
                //double canvasWidth = getWidth();
                double canvasHeight = getWidth();
                double canvasWidth = getHeight();
                double cellHeight = canvasHeight / maze.length;
                double cellWidth = canvasWidth / maze[0].length;

                try {
                    Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                    Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                    Image goalImage= new Image(new FileInputStream(ImageFileNameGoal.get()));
                    GraphicsContext gc = getGraphicsContext2D();
                    gc.clearRect(0, 0, getWidth(), getHeight());

                    //Draw Maze
                    for (int i = 0; i < maze.length; i++) {
                        for (int j = 0; j < maze[i].length; j++) {
                            if (maze[i][j] == 1) {
                                //gc.fillRect(i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                                gc.drawImage(wallImage, i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                            }
                            if(maze[i][j]==2) {

                                gc.setFill(Color.GREEN);
                                gc.fillRect(i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                            }
                        }
                    }

                    //Draw Character
                    //gc.setFill(Color.RED);
                    //gc.fillOval(characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
                    gc.drawImage(characterImage, characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
                    gc.drawImage(goalImage, goalPositionColumn*cellHeight, goalPositionRow*cellWidth,cellHeight,cellWidth );
                } catch (FileNotFoundException e) {
                    System.out.println("moo");
                }
            }
        }

        //region Properties
        private StringProperty ImageFileNameWall = new SimpleStringProperty();
        private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
        private StringProperty ImageFileNameGoal=new SimpleStringProperty();

        public void setImageFileNameGoal(String imageFileNameGoal) {
            this.ImageFileNameGoal.set(imageFileNameGoal);
        }

        public String getImageFileNameGoal() {

            return ImageFileNameGoal.get();
        }

        public StringProperty imageFileNameGoalProperty() {
            return ImageFileNameGoal;
        }

        public String getImageFileNameWall() {
            return ImageFileNameWall.get();
        }

        public void setImageFileNameWall(String imageFileNameWall) {
            this.ImageFileNameWall.set(imageFileNameWall);
        }

        public String getImageFileNameCharacter() {
            return ImageFileNameCharacter.get();
        }

        public void setImageFileNameCharacter(String imageFileNameCharacter) {
            this.ImageFileNameCharacter.set(imageFileNameCharacter);
        }
        //endregion


        public void resizeW(double width) {
            super.setWidth(width);
            // super.setHeight(height);
            redraw();
        }

        public void resizeH(double height) {
            super.setHeight(height);
            // super.setHeight(height);
            redraw();
        }

        @Override
        public boolean isResizable() {
            return true;
        }

        @Override
        public double prefWidth(double height) {
            return getWidth();
        }

        @Override
        public double prefHeight(double width) {
            return getHeight();
        }

        @Override
        public double minWidth(double height) {
            return 10;
        }

        @Override
        public double minHeight(double width) {
            return 10;
        }

        @Override
        public double maxWidth(double height) {
            return 800;
        }

        @Override
        public double maxHeight(double width) {
            return 800;
        }

        @Override
        public void resize(double width, double height) {
            super.setHeight(height);
            super.setWidth(width);
            // redraw();


        }

        public void setGoalPositionRow(int goalPositionRow) {
            this.goalPositionRow = goalPositionRow;
        }

        public void setGoalPositionColumn(int goalPositionColumn) {
            this.goalPositionColumn = goalPositionColumn;
        }

    }
