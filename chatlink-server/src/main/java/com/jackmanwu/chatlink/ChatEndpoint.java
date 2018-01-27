package com.jackmanwu.chatlink;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JackManWu on 2018/1/25.
 */
@ServerEndpoint(value = "/chat/{from}/{to}")
public class ChatEndpoint {
    private static final Map<String, Session> userSessionMap = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("from") String from) {
        System.out.println("打开连接：" + from + "--" + session);
        userSessionMap.put(from, session);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("to") String to) {
        System.out.println("服务器消息：" + message + "，当前用户名：" + to);
        for (String u : userSessionMap.keySet()) {
            System.out.println("缓存值：" + u + ">>" + userSessionMap.get(u).getId());
        }
        try {
            Session session = userSessionMap.get(to);
            System.out.println("取得session：" + session.getId());
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(@PathParam("from") String from) {
        System.out.println("删除session：" + from);
        userSessionMap.remove(from);
    }

    @OnError
    public void onError(Throwable throwable) {
        System.out.println("报错");
        System.out.println(throwable.getMessage());
    }
}
