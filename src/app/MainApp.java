package app;/**
 * Created by wujinlei on 2018/1/23.
 */

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import redis.clients.jedis.Jedis;

import java.io.IOException;

public class MainApp extends Application {

    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.stage.setTitle("chatlink");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/chatlink.fxml"));
        try {
            VBox vBox = loader.load();
            WebView webView = (WebView) vBox.getChildren().get(0);

            Jedis jedis = new Jedis("127.0.0.1", 6379);
            WebEngine webEngine = webView.getEngine();
            webEngine.loadContent(jedis.get("user"));
            jedis.close();
            webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
                @Override
                public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                    if(newValue== Worker.State.SUCCEEDED){
                        webEngine.executeScript("window.scrollTo(0,document.body.scrollHeight)");
                    }
                }
            });

            Scene scene = new Scene(vBox);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
