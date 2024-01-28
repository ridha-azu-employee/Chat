package com.azu.chat.chat_collection.general_chat.general_chat_messages.services;

import com.azu.hospital.chat_collection.chat_user.dao.ChatUserDao;
import com.azu.hospital.chat_collection.chat_user.entity.ChatUser;
import com.azu.hospital.chat_collection.general_chat.dao.GeneralChatDao;
import com.azu.hospital.chat_collection.general_chat.entity.GeneralChat;
import com.azu.hospital.chat_collection.general_chat.general_chat_messages.dao.GeneralChatMessageDao;
import com.azu.hospital.chat_collection.general_chat.general_chat_messages.dto.GeneralMessageDto;
import com.azu.hospital.chat_collection.general_chat.general_chat_messages.dto.GeneralMessageDtoMapper;
import com.azu.hospital.chat_collection.general_chat.general_chat_messages.entity.GeneralChatMessages;
import com.azu.hospital.security.newsecurity.config.JwtService;
import com.azu.hospital.utils.Generic.GenericBaseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeneralChatMessageService extends GenericBaseService {

    private static GeneralChatMessageDao generalChatMessageDao;

    private final GeneralMessageDtoMapper mapper;
    private static GeneralChatDao generalChatDao;
    private static ChatUserDao chatUserDao;


    public GeneralChatMessageService(
            @Qualifier("GeneralChatMessageDataAccessJpa") GeneralChatMessageDao generalChatMessageDao,
            @Qualifier("generalMessageDtoMapper") GeneralMessageDtoMapper mapper,
            @Qualifier("GeneralChatDataAccessJpa") GeneralChatDao generalChatDao ,
            ChatUserDao chatUserDao,
            HttpServletRequest request ,
            JwtService jwtService
    ) {
        super(jwtService , request );
        this.generalChatMessageDao = generalChatMessageDao;
        this.mapper = mapper;
        this.generalChatDao = generalChatDao;
        this.chatUserDao = chatUserDao;
    }


    public static GeneralChatMessages getMessageById(String messageId) {
        return generalChatMessageDao.getMessageById(messageId).orElse(null);
    }


    public static void AddMessageToChat(GeneralChatMessages message) {

        GeneralChat chat = generalChatDao.getAll().get(0);
        message.setChat(chat);
        GeneralChatMessages messages = generalChatMessageDao.createNewMessage(message);
        chat.getMessages().add(messages);
        generalChatDao.createNewChat(chat);

    }



    public Page<GeneralMessageDto> getGeneralChatMessages(Pageable pageable) {

        return generalChatMessageDao.getChatMessages(pageable).map(
                m -> {
                    GeneralMessageDto messageDto = mapper.apply(m);

                    if (m.getSenderId() == authId()) {
                        messageDto.setIsSender(true);
                    }
                    return messageDto;
                }
        );

    }


    public static void addSeenToMessage(Long userId) {

        List<GeneralChatMessages> messages = generalChatMessageDao.getNotSeenMessages(userId)
                .stream()
                .filter(m -> m.getSeenBy().stream().noneMatch(u -> u.getUserId() != null && u.getUserId().equals(userId)))
                .toList();

        ChatUser user = chatUserDao.findByUserId(userId);

        System.out.println(messages.size());
        List<GeneralChatMessages> messagesList = new ArrayList<>();

        for (GeneralChatMessages message : messages) {
            message.getSeenBy().add(user);
            messagesList.add(message);
        }
        generalChatMessageDao.updateAll(messagesList);

    }
}
