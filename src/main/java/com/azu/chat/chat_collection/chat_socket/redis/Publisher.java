package com.azu.chat.chat_collection.chat_socket.redis;


import com.azu.hospital.chat_collection.chat_socket.websockets.WebSocketSessionManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Objects;

@Component
public class Publisher {

    private final WebSocketSessionManager webSocketSessionManager;

    RedisClient client;

    public Publisher(WebSocketSessionManager webSocketSessionManager){
        this.webSocketSessionManager = webSocketSessionManager;
        this.client = RedisClient.create("redis://localhost:6379");
    }

    public void publish(String channel, Object message) throws JsonProcessingException {
        var connection = this.client.connect();
        ObjectMapper objectMapper = new ObjectMapper();
        connection.sync().publish(channel,objectMapper.writeValueAsString(message));
    }


    public void publishToGeneral(Long userId,Object message) throws JsonProcessingException {
        var connection = this.client.connect();
        ObjectMapper objectMapper = new ObjectMapper();

        for (Map.Entry<String, WebSocketSession> entry : webSocketSessionManager.webSocketSessionByUserId.entrySet()) {
            if (!Objects.equals(entry.getKey(), userId.toString()) && !Objects.equals(entry.getKey(), "General-" + userId)){
                connection.sync().publish(entry.getKey(),objectMapper.writeValueAsString(message));
            }

        }
    }







}
