package com.jackmanwu.chatlink;

import redis.clients.jedis.Jedis;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JackManWu on 2018/1/25.
 */
@ServerEndpoint(value = "/chat/{sender}/{receiver}", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ChatEndpoint {
    private static final Map<String, Session> userSessionMap = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("sender") String sender, @PathParam("receiver") String receiver) {
        System.out.println("打开连接：" + sender + "--" + session);
        userSessionMap.put(sender, session);
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        String key = receiver + ":" + sender;
        List<String> list = jedis.lrange(key, 0, -1);
        jedis.close();
        System.out.println("消息大小：" + list.size());
        try {
            if (list.size() > 0) {
                session.getBasicRemote().sendObject(list);
            }
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, @PathParam("sender") String sender, @PathParam("receiver") String receiver) {
        System.out.println("服务器消息：" + message + "，当前用户名：" + receiver);
        for (String u : userSessionMap.keySet()) {
            System.out.println("缓存值：" + u + ">>" + userSessionMap.get(u).getId());
        }
        try {
            Session session = userSessionMap.get(receiver);
            if (session == null) {
                Jedis jedis = new Jedis("127.0.0.1", 6379);
                String key = sender + ":" + receiver;
                jedis.rpush(key, message);
                jedis.close();
            } else {
                System.out.println("取得session：" + session.getId());
                List<String> list = new ArrayList<>();
                list.add(message);
                session.getBasicRemote().sendObject(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(@PathParam("sender") String sender) {
        System.out.println("删除session：" + sender);
        userSessionMap.remove(sender);
    }

    @OnError
    public void onError(Throwable throwable) {
        System.out.println("报错");
        System.out.println(throwable.getMessage());
    }
}
