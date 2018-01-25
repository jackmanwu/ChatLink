package app;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;


/**
 * Created by JackManWu on 2018/1/24.
 */
public class ChatLinkController {
    private static Socket socket;
    @FXML
    private TextArea message;

    @FXML
    private WebView showMessage;

    @FXML
    private Button button;

    @FXML
    private void sendMessage() {
        String key = "user";
        String msg = message.getText();
        if ("".equals(msg)) {
            return;
        }

        try {
            if (socket == null) {
                socket = new Socket("127.0.0.1", 8918);
            }
            Writer writer = new OutputStreamWriter(socket.getOutputStream());
            writer.write(msg);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        WebEngine webEngine = showMessage.getEngine();
        String msgP = "<p style='padding:12 0 0 12;'><label style='background-color:#98E165;padding:8px;font-size:14;'>" + msg + "</label></p>";
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        if (jedis.exists(key)) {
            jedis.append(key, msgP);
        } else {
            jedis.set(key, msgP);
        }
        webEngine.loadContent(jedis.get(key));
        jedis.close();

        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue == Worker.State.SUCCEEDED) {
                    webEngine.executeScript("window.scrollTo(0,document.body.scrollHeight)");
                }
            }
        });
        message.setText("");
    }

    @FXML
    private void onMouseEnterBtn() {
        button.setStyle("-fx-background-color: #129611;-fx-cursor:hand;");
    }

    @FXML
    private void onMouseExitBtn() {
        button.setStyle("-fx-background-color: #F5F5F5;");
    }
}
