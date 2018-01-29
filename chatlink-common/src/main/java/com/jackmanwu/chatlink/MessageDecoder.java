package com.jackmanwu.chatlink;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.util.List;

/**
 * Created by JackManWu on 2018/1/26.
 */
public class MessageDecoder implements Decoder.Text<List<String>> {
    private static Gson gson = new Gson();

    @Override
    public List<String> decode(String s) throws DecodeException {
        TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {
        };
        return gson.fromJson(s, typeToken.getType());
    }

    @Override
    public boolean willDecode(String s) {
        return s != null;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
