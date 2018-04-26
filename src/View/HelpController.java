package View;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;


import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable{

    @FXML
    ListView<String> listView;
    @FXML
    TextArea textArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> data = FXCollections.observableArrayList(
                "Rules","Player Movement","Generate Algorithm",
                "Solving Algorithm","Thread-Pool Capacity (Advanced)",
                "Hint",""       //5 Items.
        );
        listView.setItems(data);
        textArea.setEditable(false);



        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Your action here

                switch (newValue){
                    case "Rules": textArea.setText("The goal : to get to the" +
                            " spaceship \n" +
                            "In your way you can be assisted\n" +
                            " by hint and get the next square\n" +
                            " you need to go to.\n" +
                            "After you generated a maze you\n" +
                            " cant generate another one until\n" +
                            " you solve it.\n" +
                            "You can get one hint at the time. \n" +
                            "After you get one you should move one\n " +
                            "way or another to get another one.");
                    break;
                    case "Player Movement":textArea.setText("To move you can use\n" +
                            " or the key board or the mouse. \n" +
                            "To use the mouse you should\n" +
                            " just drag the character \n" +
                            "The key board buttons you can\n" +
                            " use to move are: \n" +
                            "Row up to move up \n" +
                            "row down to move down \n" +
                            "Row left to move left \n" +
                            "Row right to move right");
                    break;
                    case "Generate Algorithm": textArea.setText("Generate Algorithm");
                    break;
                    case "Solving Algorithm":textArea.setText("Solving Algorithm");
                    break;
                    case "Thread-Pool Capacity (Advanced)":textArea.setText("Thread-Pool Capacity (Advanced)");
                    break;
                    case "Hint":textArea.setText("Hint");
                    break;


                }
            }
        });
    }
}
