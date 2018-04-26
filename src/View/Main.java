package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        MyModel model = new MyModel();
        model.startServers();
        MyViewModel viewModel = new MyViewModel(model);
        model.addObserver(viewModel);
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());

        primaryStage.setTitle("Space Mission");
        Scene scene = new Scene(root, 900, 900);
        primaryStage.setScene(scene);
        scene.getStylesheets().add
                (Main.class.getResource("MyView.css").toExternalForm());
        MyViewController controllerView = fxmlLoader.getController();
        controllerView.setPrimaryStage(primaryStage.getOwner());
        controllerView.setFxmlLoader(fxmlLoader);
        controllerView.setPrimaryStage(primaryStage.getOwner());
        controllerView.setResizeEvent(scene);
        controllerView.setViewModel(viewModel);
        controllerView.setStage(primaryStage);
        controllerView.setRoot(root);
        viewModel.addObserver(controllerView);
        primaryStage.setOnCloseRequest(e->{
            e.consume();

            controllerView.ShutDown();

        });
        root.setOnScroll(evt -> {
            if (evt.isControlDown()) {
                evt.consume();
                if (evt.getDeltaY()<=0) {
                    primaryStage.setWidth(primaryStage.getWidth() * 0.95);
                    primaryStage.setHeight(primaryStage.getHeight() * 0.95);
                }else{
                    primaryStage.setWidth(primaryStage.getWidth() * 1.05);
                    primaryStage.setHeight(primaryStage.getHeight() * 1.05);
                }
            }
        });

        //SetStageCloseEvent(primaryStage);
        primaryStage.show();
    }

    /*
    private void SetStageCloseEvent(Stage primaryStage,MyModel model) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    model.ShutDown();
                }
                windowEvent.consume();
            }

    });
    }
*/
    private void SetStageCloseEvent(Stage primaryStage, MyModel model) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    model.stopServers();


                }

                windowEvent.consume();

            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}
