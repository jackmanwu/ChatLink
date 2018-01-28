package app;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by JackManWu on 2018/1/24.
 */
@ClientEndpoint
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
        URI uri = URI.create("ws://localhost:8080/chat/xiaoming/wujinlei");
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
    public void onMessage(Session session, String message) {
        System.out.println("收到消息：" + message + "，session：" + session.getId());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                WebEngine webEngine = showMessage.getEngine();
                webEngine.executeScript("addChildNode(\'" + message + "\','left')");
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
        String msg = message.getText();
        if ("".equals(msg)) {
            return;
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                WebEngine webEngine = showMessage.getEngine();
                webEngine.executeScript("addChildNode(\'" + msg + "\','right')");
                webEngine.executeScript("window.scrollTo(0,document.body.scrollHeight)");
            }
        });

        System.out.println("当前会话：" + session);
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
