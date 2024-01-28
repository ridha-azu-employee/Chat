package com.azu.chat.chat_collection.chat_socket.websockets;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

@Component
public class WebSocketSessionManager {

    public final Map<String, WebSocketSession> webSocketSessionByUserId = new HashMap<>();

    public void addWebSocketSession(WebSocketSession webSocketSession) {
        String userId = WebSocketHelper.getUserIdFromSessionAttribute(webSocketSession);
        this.webSocketSessionByUserId.put(userId, webSocketSession);
        this.webSocketSessionByUserId.put("General-" + userId, webSocketSession);
    }

    public void removeWebSocketSession(WebSocketSession webSocketSession) {
        String userId = WebSocketHelper.getUserIdFromSessionAttribute(webSocketSession);
        this.webSocketSessionByUserId.remove(userId);
        this.webSocketSessionByUserId.remove("General" + userId);
    }

    public WebSocketSession getWebSocketSessions(String userId) {
        return this.webSocketSessionByUserId.get(userId);
    }



}
