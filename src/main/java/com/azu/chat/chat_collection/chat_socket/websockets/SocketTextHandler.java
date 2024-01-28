package com.azu.chat.chat_collection.chat_socket.websockets;


import com.azu.hospital.chat_collection.chat.chat_message.entity.Message;
import com.azu.hospital.chat_collection.chat.chat_message.services.MessageServices;
import com.azu.hospital.chat_collection.chat.entity.Chat;
import com.azu.hospital.chat_collection.chat.services.ChatService;
import com.azu.hospital.chat_collection.chat_socket.redis.Publisher;
import com.azu.hospital.chat_collection.chat_socket.redis.Subscriber;
import com.azu.hospital.chat_collection.chat_user.services.UserChatService;
import com.azu.hospital.security.newsecurity.config.JwtService;
import com.azu.hospital.utils.enums.chat.MessageType;
import com.azu.hospital.utils.enums.chat.UserConnectState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Objects;


@Component
public class SocketTextHandler extends TextWebSocketHandler {

    private final WebSocketSessionManager webSocketSessionManager;
    private final Publisher publisher;
    private final Subscriber subscriber;
    private final JwtService jwtService;


    @Autowired
    public SocketTextHandler(
            WebSocketSessionManager webSocketSessionManager,
            Publisher redisPublisher,
            Subscriber redisSubscriber,
            JwtService jwtService
    ) {
        this.webSocketSessionManager = webSocketSessionManager;
        this.publisher = redisPublisher;
        this.subscriber = redisSubscriber;
        this.jwtService = jwtService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.webSocketSessionManager.addWebSocketSession(session);
        Long userId = WebSocketHelper.getUserIdFromSessionToken(session, jwtService);
        if (userId == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid or missing session token"));
            return;
        }
        this.subscriber.subscribe(userId.toString());

        Long authId = WebSocketHelper.getUserIdFromSessionToken(session, jwtService);
        UserChatService.updateUserOnline(authId);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        this.webSocketSessionManager.removeWebSocketSession(session);
        Long userId = WebSocketHelper.getUserIdFromSessionToken(session, jwtService);
        if (userId == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid or missing session token"));
            return;
        }
        this.subscriber.unsubscribe(userId.toString());
        UserChatService.updateUserOfLine(userId);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws IOException {


        Long userId = WebSocketHelper.getUserIdFromSessionToken(session, jwtService);

        if (userId == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid or missing session token"));
            return;
        }

        String payload = message.getPayload();


        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(payload);

            String targetId = jsonNode.get("targetId").asText();
            String messageToBeSent = jsonNode.get("message").asText();

            String messageType;

            Chat chat = ChatService.getChatByChatId(targetId);


            if (chat == null) {
                try {
                    chat = ChatService.checkChat(userId, Long.valueOf(targetId));
                } catch (Exception e) {
                    ObjectMapper errorMapper = new ObjectMapper();
                    ObjectNode errorJson = errorMapper.createObjectNode();

                    errorJson.put("message", "Invalid Id");
                    errorJson.put("code", 422);

                    this.publisher.publish(userId.toString(), errorJson);
                    return;

                }
            }

            String userConnectionState = WebSocketHelper.getUserConnectionState(webSocketSessionManager,
                    Long.valueOf(chat.getMessageFirstId() == userId ? chat.getMessageSecondId() : chat.getMessageFirstId()));

            Message newMessage = new Message(messageToBeSent, userId);

            if (Objects.nonNull(jsonNode.get("messageType"))) {

                messageType = jsonNode.get("messageType").asText();


                if (!messageType.equals("Text") && !messageType.equals("File")) {
                    this.publisher.publish(userId.toString(), getMessageObject("messageType : must be Text Or File"));
                    return;
                }

                newMessage.setType(MessageType.valueOf(messageType));

                if (messageType.equals("File")) {
                    if (Objects.isNull(jsonNode.get("fileType")) || Objects.isNull(jsonNode.get("filePath"))) {
                        this.publisher.publish(userId.toString(), getMessageObject("fileType and filePath required"));
                        return;
                    }

                    newMessage.setFileType(jsonNode.get("fileType").asText());
                    newMessage.setFilePath(jsonNode.get("filePath").asText());

                }
            }

            if (Objects.nonNull(jsonNode.get("replyMessage"))) {
                Message replayMessage = MessageServices.getMessageById(jsonNode.get("replyMessage").asText());
                if (Objects.isNull(replayMessage)) {
                    this.publisher.publish(userId.toString(), getMessageObject("message not found"));
                    return;
                }

                newMessage.setIsReply(true);
                newMessage.setReplyMessage(replayMessage);
            }

            if (Objects.equals(UserConnectState.Connection.toString(), userConnectionState)) {
                newMessage.setIsReceived(true);
                newMessage.setIsSeen(false);
            } else if (Objects.equals(UserConnectState.OnChatScreen.toString(), userConnectionState)) {
                newMessage.setIsReceived(true);
                newMessage.setIsSeen(false);
            } else if (Objects.equals(chat.getId(), userConnectionState)) {
                newMessage.setIsSeen(true);
                newMessage.setIsReceived(true);
            } else {
                newMessage.setIsSeen(false);
                newMessage.setIsReceived(false);
            }

            // TODO: 1/8/2024 Here Send Notifications

            MessageServices.AddMessageToChat(chat, newMessage);

            this.publisher.publish(targetId, MessageServices.GetMessageDto(newMessage));


        } catch (JsonProcessingException e) {

            ObjectMapper errorMapper = new ObjectMapper();
            ObjectNode errorJson = errorMapper.createObjectNode();

            errorJson.put("message", "Invalid Data Format");
            errorJson.put("code", 422);

            this.publisher.publish(userId.toString(), errorJson);
        }
    }


    private ObjectNode getMessageObject(String message) {
        ObjectMapper errorMapper = new ObjectMapper();
        ObjectNode errorJson = errorMapper.createObjectNode();

        errorJson.put("message", message);
        errorJson.put("code", 422);

        return errorJson;
    }
}
