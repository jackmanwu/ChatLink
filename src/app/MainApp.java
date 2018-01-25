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
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("chatlink");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/chatlink.fxml"));
        try {
            VBox vBox = loader.load();
            WebView webView = (WebView) vBox.getChildren().get(0);

            Jedis jedis = new Jedis("127.0.0.1", 6379);
            WebEngine webEngine = webView.getEngine();

            ServerSocket serverSocket = new ServerSocket(8918);
            Socket socket = serverSocket.accept();
            Reader reader = new InputStreamReader(socket.getInputStream());
            char[] chars = new char[1024];
            int len;
            StringBuilder stringBuilder = new StringBuilder();
            while ((len = reader.read(chars)) != -1) {
                stringBuilder.append(new String(chars, 0, len));
            }
            reader.close();
            socket.close();
            serverSocket.close();
            System.out.println("接收数据：" + stringBuilder.toString());

            String myMsg = jedis.get("user");
            String otherMsg = "<p style='padding:12 0 0 12;'><label style='background-color:#FF0000;padding:8px;font-size:14;'>" + stringBuilder.toString() + "</label></p>";
            if(myMsg!=null){
                webEngine.loadContent(myMsg+ otherMsg);
            }
            jedis.close();
            webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
                @Override
                public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                    if (newValue == Worker.State.SUCCEEDED) {
                        webEngine.executeScript("window.scrollTo(0,document.body.scrollHeight)");
                    }
                }
            });

            Scene scene = new Scene(vBox);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
