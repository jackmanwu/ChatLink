package app;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import redis.clients.jedis.Jedis;


/**
 * Created by JackManWu on 2018/1/24.
 */
public class ChatLinkController {
    @FXML
    private TextArea message;

    @FXML
    private WebView showMessage;

    @FXML
    private void sendMessage() {
        String key = "user";
        String msg = message.getText();
        if ("".equals(msg)) {
            return;
        }

        WebEngine webEngine = showMessage.getEngine();
        String msgP = "<p><label style='background-color:#98E165;padding:5px 5px;font-size:12;'>" + msg + "</label></p>";
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
                if(newValue== Worker.State.SUCCEEDED){
                    webEngine.executeScript("window.scrollTo(0,document.body.scrollHeight)");
                }
            }
        });
    }
}
