package app;

import com.jackmanwu.chatlink.MessageDecoder;
import com.jackmanwu.chatlink.MessageEncoder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Created by JackManWu on 2018/1/24.
 */
@ClientEndpoint(encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ChatLinkController implements Initializable {
    @FXML
    private TextArea message;

    @FXML
    private WebView showMessage;

    @FXML
    private Button button;

    private Session session;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WebEngine webEngine = showMessage.getEngine();
        webEngine.load(ChatLinkController.class.getResource("templates/message.html").toString());
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        URI uri = URI.create("ws://localhost:8080/chat/xiaoming/laowang");
        try {
            container.connectToServer(this, uri);
        } catch (DeploymentException | IOException e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("会话：" + session.getId());
        this.session = session;
    }

    @OnMessage
    public void onMessage(Session session, List<String> list) {
        System.out.println("收到消息：" + message + "，session：" + session.getId());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                WebEngine webEngine = showMessage.getEngine();
                list.forEach(msg -> webEngine.executeScript("addChildNode(\'" + msg + "\','left')"));
                webEngine.executeScript("window.scrollTo(0,document.body.scrollHeight)");
            }
        });
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("已经关闭了，session：" + session.getId());
    }

    @FXML
    private void sendMessage() {
        sendMsg();
    }

    @FXML
    private void onMouseEnterBtn() {
        button.setStyle("-fx-background-color: #129611;-fx-cursor:hand;");
    }

    @FXML
    private void onMouseExitBtn() {
        button.setStyle("-fx-background-color: #F5F5F5;");
    }

    @FXML
    private void EnterSendMsgEvent() {
        message.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String msg = message.getText();
                if ("\n".equals(msg) || "\n\n".equals(msg)) {
                    message.setText("");
                } else {
                    sendMsg();
                }
            }
        });
    }

    private void sendMsg() {
        String msg = message.getText();
        System.out.println("发出的消息：" + msg);

        String resultMsg = msg.replace("\n", "");
        if ("".equals(resultMsg)) {
            return;
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                WebEngine webEngine = showMessage.getEngine();
                webEngine.executeScript("addChildNode(\'" + resultMsg + "\','right')");
                webEngine.executeScript("window.scrollTo(0,document.body.scrollHeight)");
            }
        });

        try {
            session.getBasicRemote().sendText(resultMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        message.setText("");
    }
}
