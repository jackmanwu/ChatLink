import javax.websocket.*;
import java.net.URI;

/**
 * Created by JackManWu on 2018/1/26.
 */
@ClientEndpoint
public class Test {
    private Session session;

    public static void main(String[] args) throws Exception {
        URI uri = new URI("ws://localhost:8080/chat/wujinlei");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        Test test = new Test();
        test.session = container.connectToServer(Test.class, uri);
        System.out.println(test.session.getId());
        test.session.getBasicRemote().sendText("Hello");
        while(true){
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("收到消息：" + message);
    }

    @OnClose
    public void onClose() {
        System.out.println("已经关闭了");
    }
}
