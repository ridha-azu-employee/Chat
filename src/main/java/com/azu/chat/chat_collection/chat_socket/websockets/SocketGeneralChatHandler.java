package com.azu.chat.chat_collection.chat_socket.websockets;

import com.azu.hospital.chat_collection.chat_socket.redis.Publisher;
import com.azu.hospital.chat_collection.chat_socket.redis.Subscriber;
import com.azu.hospital.chat_collection.general_chat.dao.GeneralChatDao;
import com.azu.hospital.chat_collection.general_chat.entity.GeneralChat;
import com.azu.hospital.chat_collection.general_chat.general_chat_messages.entity.GeneralChatMessages;
import com.azu.hospital.chat_collection.general_chat.general_chat_messages.services.GeneralChatMessageService;
import com.azu.hospital.security.newsecurity.config.JwtService;
import com.azu.hospital.utils.enums.chat.MessageType;
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
public class SocketGeneralChatHandler extends TextWebSocketHandler {


    private final WebSocketSessionManager webSocketSessionManager;
    private final Publisher publisher;
    private final Subscriber subscriber;
    private final JwtService jwtService;

    private final GeneralChatDao chatDao;

    @Autowired
    public SocketGeneralChatHandler(
            WebSocketSessionManager webSocketSessionManager,
            Publisher publisher,
            Subscriber subscriber,
            JwtService jwtService,
            GeneralChatDao chatDao
    ) {
        this.webSocketSessionManager = webSocketSessionManager;
        this.publisher = publisher;
        this.subscriber = subscriber;
        this.jwtService = jwtService;
        this.chatDao = chatDao;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.webSocketSessionManager.addWebSocketSession(session);
        Long authId = WebSocketHelper.getUserIdFromSessionToken(session, jwtService);

        if (chatDao.countChat() != 1) {
            GeneralChat chat = chatDao.createNewChat(new GeneralChat());
            chatDao.createNewChat(chat);
        }

        this.subscriber.subscribe("General-"+authId);

        GeneralChatMessageService.addSeenToMessage(authId);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        this.webSocketSessionManager.removeWebSocketSession(session);
        Long userId = WebSocketHelper.getUserIdFromSessionToken(session, jwtService);
        if (userId == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid or missing session token"));
            return;
        }
        this.subscriber.subscribe("General-"+userId);

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

            String messageToBeSent = jsonNode.get("message").asText();

            String messageType;

            GeneralChatMessages messages = new GeneralChatMessages(messageToBeSent, userId);

            messages.setType(MessageType.Text);

            if (Objects.nonNull(jsonNode.get("messageType"))) {

                messageType = jsonNode.get("messageType").asText();


                if (!messageType.equals("Text") && !messageType.equals("File")) {
                    this.publisher.publish(userId.toString(), getMessageObject("messageType : must be Text Or File"));
                    return;
                }

                messages.setType(MessageType.valueOf(messageType));

                if (messageType.equals("File")) {
                    if (Objects.isNull(jsonNode.get("fileType")) || Objects.isNull(jsonNode.get("filePath"))) {
                        this.publisher.publish(userId.toString(), getMessageObject("fileType and filePath required"));
                        return;
                    }

                    messages.setFileType(jsonNode.get("fileType").asText());
                    messages.setFilePath(jsonNode.get("filePath").asText());

                }
            }

            if (Objects.nonNull(jsonNode.get("replyMessage"))) {
                GeneralChatMessages replayMessage = GeneralChatMessageService.getMessageById(jsonNode.get("replyMessage").asText());
                if (Objects.isNull(replayMessage)) {
                    this.publisher.publish(userId.toString(), getMessageObject("message not found"));
                    return;
                }

                messages.setIsReply(true);
                messages.setReplyMessage(replayMessage);
            }



            GeneralChatMessageService.AddMessageToChat(messages);


            this.publisher.publishToGeneral(userId,"hello");



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
