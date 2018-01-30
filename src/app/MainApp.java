package app;/**
 * Created by wujinlei on 2018/1/23.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("laowang");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/chatlink.fxml"));
        try {
            VBox vBox = loader.load();
            Scene scene = new Scene(vBox);
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("platform.jpg")));
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
