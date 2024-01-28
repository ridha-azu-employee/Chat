package com.azu.chat.chat_collection.chat_socket.redis;

import com.azu.hospital.chat_collection.chat_socket.websockets.WebSocketSessionManager;
import io.lettuce.core.pubsub.RedisPubSubListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

@Service
public class SubscriberHelper implements RedisPubSubListener<String , String> {

    private final WebSocketSessionManager webSocketSessionManager;

    @Autowired
    public SubscriberHelper(WebSocketSessionManager webSocketSessionManager){
        this.webSocketSessionManager = webSocketSessionManager;
    }

    @Override
    public void message(String channel, String message) {
        var ws = this.webSocketSessionManager.getWebSocketSessions(channel);
        if (ws != null) {
            try {
                ws.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("No WebSocket sessions available for channel: " + channel);
        }
    }

    @Override
    public void message(String s, String k1, String s2) {

    }

    @Override
    public void subscribed(String s, long l) {

    }

    @Override
    public void psubscribed(String s, long l) {

    }

    @Override
    public void unsubscribed(String s, long l) {

    }

    @Override
    public void punsubscribed(String s, long l) {

    }
}
