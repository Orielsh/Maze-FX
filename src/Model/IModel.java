package Model;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.io.File;

/**
 * Created by elias_000 on 21/06/2017.
 */
public interface IModel {

    public int getCharacterPositionRow();
    public int getCharacterPositionColumn();
    public void generateMaze(int width, int height);
    public int[][] getMaze();
    public void Solve();
    public void Save(File file);
    public void OpenFile(File chosen);
    public void moveCharacter(KeyCode movement);
    public int getGoalPositionRow();
    public int getGoalPositionColumn();
    public boolean isWin();
    public void HintM();
    public void MousePresse(int row,int col);
    public void stopServers();
    public void ShutDown();
}
