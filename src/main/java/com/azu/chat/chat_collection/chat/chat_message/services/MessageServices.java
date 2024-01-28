package com.azu.chat.chat_collection.chat.chat_message.services;

import com.azu.hospital.chat_collection.chat.chat_message.dao.MessageDao;
import com.azu.hospital.chat_collection.chat.chat_message.dto.MessageDto;
import com.azu.hospital.chat_collection.chat.chat_message.dto.MessageDtoMapper;
import com.azu.hospital.chat_collection.chat.chat_message.entity.Message;
import com.azu.hospital.chat_collection.chat.dao.ChatDao;
import com.azu.hospital.chat_collection.chat.entity.Chat;
import com.azu.hospital.exceptions.ResourceNotFoundException;
import com.azu.hospital.security.newsecurity.config.JwtService;
import com.azu.hospital.utils.Generic.GenericBaseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MessageServices extends GenericBaseService {

    private static ChatDao chatDao;

    private static MessageDao messageDao;

    private static MessageDtoMapper mapper;

    public MessageServices(
            ChatDao chatDao,
            MessageDao messageDao,
            MessageDtoMapper mapper,
            JwtService jwtService,
            HttpServletRequest request
    ) {
        super(jwtService, request);
        this.chatDao = chatDao;
        this.messageDao = messageDao;
        this.mapper = mapper;
    }


    public static void AddMessageToChat(Chat chat, Message message) {
        message.setChat(chat);
        message = messageDao.createNewMessage(message);
        chat.getMessages().add(message);
        chatDao.updateChat(chat);
    }


    public static MessageDto GetMessageDto(Message message) {
        return mapper.apply(message);
    }

    public static Message getMessageById(String messageId) {
        return messageDao.getMessageById(messageId).orElse(null);
    }

    public static void updateAllMessageReceived(Long userId) {

        List<Chat> chats = chatDao.findAllChatsByUserId(userId);

        List<Message> messages = new ArrayList<>();

        for (Chat chat : chats) {
            List<Message> me = chat.getMessages();
            for (Message message : me) {
                if (!Objects.equals(message.getSenderId(), userId) && !message.getIsReceived()) {
                    message.setIsReceived(true);
                    messages.add(message);
                }
            }
        }
        messageDao.updateListOfMessage(messages);

    }

    public static void updateAllMessageSeen(String chatId, Long userId) {

        Chat chat = chatDao.findById(chatId).orElseThrow(
                () -> new ResourceNotFoundException("Chat not found")
        );

        List<Message> messages = new ArrayList<>();

        for (Message message : chat.getMessages()) {
            if (!Objects.equals(message.getSenderId(), userId) && (!message.getIsSeen() || !message.getIsReceived()) ) {
                message.setIsReceived(true);
                message.setIsSeen(true);
                messages.add(message);
            }
        }

        messageDao.updateListOfMessage(messages);

    }

    public Page<MessageDto> getChatMessages(String chatId, Pageable pageable) {

        chatDao.findById(chatId).orElseThrow(
                () -> new ResourceNotFoundException("Chat not found")
        );

        return messageDao.getChatMessages(chatId, pageable).map(
                m -> {
                    MessageDto messageDto = mapper.apply(m);

                    if (m.getSenderId() == authId()) {
                        messageDto.setIsSender(true);
                    }
                    return messageDto;
                }
        );

    }

}
