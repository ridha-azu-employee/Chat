package com.azu.chat.chat_collection.chat_socket.websockets;

import com.azu.hospital.security.newsecurity.config.JwtService;
import com.azu.hospital.utils.enums.chat.UserConnectState;
import io.jsonwebtoken.Claims;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketHelper {
    public static String userIdKey = "userId";

    private final WebSocketSessionManager webSocketSessionManager;

    public WebSocketHelper(WebSocketSessionManager webSocketSessionManager) {
        this.webSocketSessionManager = webSocketSessionManager;
    }

    public static String getUserIdFromSessionAttribute(WebSocketSession webSocketSession) {
        return (String) webSocketSession.getAttributes().get(userIdKey);
    }


    public static Long getUserIdFromSessionToken(WebSocketSession session, JwtService jwtService) {
        try {
            String token = session.getHandshakeHeaders().get("authorization").get(0);
            Claims claims = jwtService.extractAllClaims(token.replace("Bearer ", ""));
            return Long.valueOf(claims.get("userId").toString());
        } catch (Exception e) {
            return null;
        }

    }

    public static String getUserIdFromUrl(String path) {
        return path.substring(path.lastIndexOf('/') + 1);
    }


    public static String getUserConnectionState(WebSocketSessionManager sessionManager, Long userId) {

        try {
            String uri = sessionManager.getWebSocketSessions(String.valueOf(userId)).getUri().getPath();


            if (uri.equals("/users")) {
                return UserConnectState.Connection.toString();
            } else if (uri.equals("/users/chat")) {
                return UserConnectState.OnChatScreen.toString();
            } else if (uri.startsWith("/users/chat/")) {
                String[] parts = uri.split("/");
                if (parts.length == 4) {
                    return parts[3];
                }
            }

            return "Unknown";
        } catch (Exception e) {
            return UserConnectState.NoConnection.toString();
        }

    }




}
