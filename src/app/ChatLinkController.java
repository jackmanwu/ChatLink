package app;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;

/**
 * Created by wujinlei on 2018/1/23.
 */
public class ChatLinkController {
    @FXML
    private TextArea message;

    @FXML
    private TextArea showMsg;

    @FXML
    public void getMessage() {
        System.out.println("消息: " + message.getText());
        showMsg.setText(message.getText());
    }
}
