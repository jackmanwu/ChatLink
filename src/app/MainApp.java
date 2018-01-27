package app;/**
 * Created by wujinlei on 2018/1/23.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("xiaoming");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/chatlink.fxml"));
        try {
            VBox vBox = loader.load();
           /* WebView webView = (WebView) vBox.getChildren().get(0);

            WebEngine webEngine = webView.getEngine();

            String otherMsg = "<p style='padding:12 0 0 12;'><label style='background-color:#FF0000;padding:8px;font-size:14;'>" + stringBuilder.toString() + "</label></p>";
            webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
                @Override
                public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                    if (newValue == Worker.State.SUCCEEDED) {
                        webEngine.executeScript("window.scrollTo(0,document.body.scrollHeight)");
                    }
                }
            });*/

            Scene scene = new Scene(vBox);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
