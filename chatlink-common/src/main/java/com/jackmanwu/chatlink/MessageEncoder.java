package com.jackmanwu.chatlink;

import com.google.gson.Gson;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.util.List;

/**
 * Created by JackManWu on 2018/1/26.
 */
public class MessageEncoder implements Encoder.Text<List<String>> {
    private static Gson gson = new Gson();

    @Override
    public String encode(List<String> list) throws EncodeException {
        return gson.toJson(list);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
