package View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertiesController implements Initializable {

    @FXML
    Button apply;
    @FXML
    Button cancel;
    @FXML
    ComboBox<String> generatingAlgorithm;
    @FXML
    ComboBox<String> searchingAlgorithm;
    @FXML
    TextField input;

    String generate,solve;
    int threadPoolCapacity;


    Stage window = new Stage();

    public void show() throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("Properties.fxml"));
        window.setTitle("Properties");
        Scene scene = new Scene(parent);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setScene(scene);








        /*//ComboBoxes also generate actions if you need to get value instantly
        comboBox.setOnAction(e -> System.out.println("User selected " + comboBox.getValue()));
        */

        window.show();
    }


    private static void updateFile(String search, String solve, String thread) {
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream("config.properties");
            // set the properties value


            prop.setProperty("numOfThreads", thread);
            prop.setProperty("searching", search);
            prop.setProperty("generateAlgorithm", solve);


            // save properties to project root folder
            prop.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        apply.setOnAction(e->{
            apply();
            ((Node)(e.getSource())).getScene().getWindow().hide();
        });
        cancel.setOnAction(e->{
            close();
            ((Node)(e.getSource())).getScene().getWindow().hide();
        });

        searchingAlgorithm.getItems().addAll(
                "BreadthFirstSearch",
                "BestFirstSearch",
                "DepthFirstSearch"
        );

        generatingAlgorithm.getItems().addAll(
                "MyMazeGenerator",
                "SimpleMazeGenerator"
        );

        Properties prop = new Properties();
        if (!(new File("config.properties").isFile())) {//file not exist=> no way. (as we start the main it run the server and initialize this file if doesn't exist).
            generate = "MyMazeGenerator";
            solve = "BreadthFirstSearch";
            threadPoolCapacity=0;
        } else {                                                //file exist.
            try {
                InputStream inputStream = new FileInputStream("config.properties");
                prop.load(inputStream);
                generate = prop.getProperty("searching");
                solve = prop.getProperty("generateAlgorithm");
                threadPoolCapacity=Integer.parseInt(prop.getProperty("numOfThreads"));
            } catch (Exception e) {         //If failed to load file then will load the window with dummy parameters.
                generate="MyMazeGenerator";
                solve="BreadthFirstSearch";
                threadPoolCapacity=3;
            }
        }

        generatingAlgorithm.setPromptText(generate);
        searchingAlgorithm.setPromptText(solve);
        input.setText(threadPoolCapacity+"");



    }

    public void apply(){

        try {
            int inputNumber = Integer.parseInt(input.getText());
            if (inputNumber < 2) inputNumber = Integer.parseInt("ASD"); //Trying to store input as integer and validate for ">=2" if not will throw exception.
            if (generatingAlgorithm.getValue()!=null)
                generatingAlgorithm.setPromptText(generatingAlgorithm.getValue());
            if (searchingAlgorithm.getValue()!=null)
                searchingAlgorithm.setPromptText(searchingAlgorithm.getValue());
            updateFile(generatingAlgorithm.getPromptText(), searchingAlgorithm.getPromptText(), input.getText());
            System.out.println("Saved");
            this.window.close();
        } catch (NumberFormatException ex) {
            AlertBox.display("Bad Parameters", "Thread-Pool capacity must be natural number");
        }
    }

    public void close(){
        System.out.println("Changes dismissed");
    }














}


