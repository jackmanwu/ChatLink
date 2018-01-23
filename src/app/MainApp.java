package app;/**
 * Created by wujinlei on 2018/1/23.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage=primaryStage;
        this.stage.setTitle("chatlink");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/chatlink.fxml"));
        try {
            VBox vBox = loader.load();
            Scene scene = new Scene(vBox);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
