package ViewModel;

import Model.IModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;
    public boolean win;
    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;
    private int goalPositionRowIndex;
    private int goalPositionColumnIndex;

    public StringProperty characterPositionRow = new SimpleStringProperty("1"); //For Binding
    public StringProperty characterPositionColumn = new SimpleStringProperty("1"); //For Binding

    public MyViewModel(IModel model){
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o==model){
            characterPositionRowIndex = model.getCharacterPositionRow();
            characterPositionRow.set(characterPositionRowIndex + "");
            characterPositionColumnIndex = model.getCharacterPositionColumn();
            characterPositionColumn.set(characterPositionColumnIndex + "");
            goalPositionColumnIndex=model.getGoalPositionColumn();
            goalPositionRowIndex=model.getGoalPositionRow();
            win=model.isWin();
            setChanged();
            notifyObservers();
        }
    }

    public void generateMaze(int width, int height){
        model.generateMaze(width, height);
    }

    public int[][] getMaze() {
        return model.getMaze();
    }

    public int getCharacterPositionRow() {
        return characterPositionRowIndex;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumnIndex;
    }

    public void Solve(int[][] maze)
    {
        model.Solve();
    }

    public void Save(File file)
    {
        model.Save(file);
    }

    public void OpenFile(File chosen)
    {
        model.OpenFile(chosen);
    }

    public void moveCharacter(KeyCode movement){
        model.moveCharacter(movement);
    }

    public int getGoalPositionRowIndex() {
        return goalPositionRowIndex;
    }

    public int getGoalPositionColumnIndex() {
        return goalPositionColumnIndex;
    }

    public void setGoalPositionRowIndex(int goalPositionRowIndex) {
        this.goalPositionRowIndex = goalPositionRowIndex;
    }

    public void setGoalPositionColumnIndex(int goalPositionColumnIndex) {
        this.goalPositionColumnIndex = goalPositionColumnIndex;
    }
    public boolean Win() {
        return win;
    }

    public void Hint()
    {
        model.HintM();
    }

    public void MouseP(int row,int col){
        model.MousePresse(row,col);
    }

    public boolean isWin() {
        return win;
    }

    public void close(){
        model.ShutDown();
    }

}
