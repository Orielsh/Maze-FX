package Model;

import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import Server.IServerStrategy;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import Server.*;
import Client.*;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyModel extends Observable implements IModel,Serializable {

    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private int[][] maze2;
    private Maze maze;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;
    private int goalPositionRow ;
    private int goalPositionColumn ;
    private boolean win;
    private Server mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
    private Server solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
    private boolean Thint=false;
    public void startServers() {
        solveSearchProblemServer.start();
        mazeGeneratingServer.start();
    }

    public void stopServers() {
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
    }


    public void generateMaze(int row, int column) {
        //Generate maze
        threadPool.execute(() -> {
           // try {
                generatingMazeClient(row, column);
                this.win=false;
                characterPositionRow = maze.getStartPosition().getY();
                characterPositionColumn = maze.getStartPosition().getX();
                goalPositionColumn= maze.getGoalPosition().getX();
                goalPositionRow=maze.getGoalPosition().getY();
                maze2=maze.getMaze();
                setChanged();
               notifyObservers();
                /*
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();

                notifyObservers();
            }
            */
        });
    }

    public int[][] getMaze() {
        return maze2;
    }


    private void generatingMazeClient (int rows, int columns){
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rows, columns};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[])((byte[])fromServer.readObject());
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[(rows * columns) + 12];
                        is.read(decompressedMaze);
                         maze = new Maze(decompressedMaze);
                        //maze.print();
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void moveCharacter(KeyCode movement) {
        Thint=false;
        if (maze2[characterPositionColumn][characterPositionRow] == 2)
            maze2[characterPositionColumn][characterPositionRow] = 0;
        switch (movement) {
            case UP:
                if(characterPositionRow-1>=0) {
                    if (maze2[characterPositionColumn][characterPositionRow - 1] == 0 || maze2[characterPositionColumn][characterPositionRow - 1] == 2) {
                        characterPositionRow--;
                        if (maze2[characterPositionColumn][characterPositionRow] == 2)
                            maze2[characterPositionColumn][characterPositionRow] = 0;
                    }
                }
                break;
            case DOWN:
                if(characterPositionRow+1<maze2[0].length)
                {
                if(maze2[characterPositionColumn][characterPositionRow+1]==0||maze2[characterPositionColumn][characterPositionRow+1]==2 ) {
                    characterPositionRow++;
                    if (maze2[characterPositionColumn][characterPositionRow] == 2)
                        maze2[characterPositionColumn][characterPositionRow] = 0;
                    }
                }
                break;
            case RIGHT:
                if( characterPositionColumn+1<maze2.length)
                {
                if(maze2[characterPositionColumn+1][characterPositionRow]==0 || maze2[characterPositionColumn+1][characterPositionRow]==2) {
                    characterPositionColumn++;
                    if (maze2[characterPositionColumn][characterPositionRow] == 2)
                        maze2[characterPositionColumn][characterPositionRow] = 0;
                }
                }
                break;
            case LEFT:
                if( characterPositionColumn-1>=0) {
                    if (maze2[characterPositionColumn - 1][characterPositionRow] == 0 || maze2[characterPositionColumn - 1][characterPositionRow] == 2) {
                        characterPositionColumn--;
                        if (maze2[characterPositionColumn][characterPositionRow] == 2)
                            maze2[characterPositionColumn][characterPositionRow] = 0;
                    }
                }
                break;
        }
        if(characterPositionRow==goalPositionRow&& characterPositionColumn==goalPositionColumn)
            win=true;
        setChanged();
        notifyObservers();
    }



    public int getCharacterPositionRow() {
        return characterPositionRow;
    }


    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    //solving the maze

    public void Solve(){
        threadPool.execute(() -> {
            // try {
            SolvingMazeClient(false);
            setChanged();
            notifyObservers();
                /*
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();

                notifyObservers();
            }
            */
        });
    }

    private void SolvingMazeClient (boolean ishint)
    {
        try {
            for(int i=0;i<maze2.length;i++)
                for(int j=0;j<maze2[0].length;j++)
                    if(maze2[i][j]==2)
                        maze2[i][j]=0;
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        maze.setStart(characterPositionColumn,characterPositionRow);
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
                        int hint;
                        if(ishint)
                                hint=2;
                        else
                                hint=mazeSolutionSteps.size();

                        for (int i = 0; i < hint; i++) {
                            int index=mazeSolutionSteps.get(i).getState().indexOf(44);
                            int s_column=Integer.parseInt(mazeSolutionSteps.get(i).getState().substring(0,index));
                            String temp=mazeSolutionSteps.get(i).getState().substring(index+1);
                            int s_row=Integer.parseInt(temp);
                            maze2[s_column][s_row]=2;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void Save(File file)
    {
        try {
            FileOutputStream saveing = new FileOutputStream(file);
            OutputStream temp = new MyCompressorOutputStream(saveing);
            maze.setStart(characterPositionColumn,characterPositionRow);
            temp.write(maze.toByteArray());
            temp.flush();
            temp.close();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void OpenFile(File chosen)
    {
        try {
            FileInputStream loadFile = new FileInputStream(chosen);
            FileInputStream temp = new FileInputStream(chosen);
            InputStream in= new MyDecompressorInputStream(loadFile);
            int columnNum=temp.read()*127+temp.read();
            int rowNum=temp.read()*127+temp.read();
            byte byteMaze[]=new byte[columnNum*rowNum+12];
            in.read(byteMaze);
            maze=new Maze(byteMaze);
            maze2=maze.getMaze();
            characterPositionRow=maze.getStartPosition().getY();
            characterPositionColumn=maze.getStartPosition().getX();
            in.close();
            loadFile.close();
            temp.close();
            setChanged();
            notifyObservers();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public int getGoalPositionRow() {
        return goalPositionRow;
    }

    public int getGoalPositionColumn() {
        return goalPositionColumn;
    }

    public boolean isWin() {
        return win;
    }

    public void HintM()
    {
        if(!Thint) {
            Thint=true;
            threadPool.execute(() -> {
                // try {
                SolvingMazeClient(true);
                setChanged();
                notifyObservers();

            });
        }

    }

    public void MousePresse(int row,int col) {
        characterPositionRow = row;
        characterPositionColumn = col;
        setChanged();
        notifyObservers();
    }

    public void ShutDown(){
        stopServers();
        threadPool.shutdown();
        setChanged();
        notifyObservers();
        Platform.exit();
    }


}
