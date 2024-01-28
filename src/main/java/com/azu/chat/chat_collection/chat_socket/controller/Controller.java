package com.azu.chat.chat_collection.chat_socket.controller;


import com.azu.hospital.chat_collection.chat_socket.websockets.SocketTextHandler;
import com.azu.hospital.chat_collection.chat_socket.websockets.WebSocketSessionManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@RestController
public class Controller {


    private final SocketTextHandler socketTextHandler;
    private final WebSocketSessionManager webSocketSessionManager;

    public Controller(
            SocketTextHandler socketTextHandler,
            WebSocketSessionManager webSocketSessionManager
    ) {
        this.socketTextHandler = socketTextHandler;
        this.webSocketSessionManager = webSocketSessionManager;
    }

    @GetMapping("send")
    public void sendMessageToUserId(@RequestParam String userId , @RequestParam String message) throws IOException {
        WebSocketSession webSocketSession = webSocketSessionManager.getWebSocketSessions(userId);

        if (webSocketSession == null){
            throw new IOException("User Doesn't Connect");
        }

        TextMessage textMessage = new TextMessage(message);
        socketTextHandler.handleTextMessage(webSocketSession , textMessage);
    }
}

