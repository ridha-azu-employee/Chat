package com.azu.chat.chat_collection.chat_user.services;


import com.azu.hospital.chat_collection.chat_user.dao.ChatUserDao;
import com.azu.hospital.chat_collection.chat_user.entity.ChatUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserChatService {

    private static ChatUserDao chatUserDao;


    public UserChatService(ChatUserDao chatUserDao) {
        this.chatUserDao = chatUserDao;
    }


    public static void updateUserOnline(Long userId){
        ChatUser chatUser = chatUserDao.findByUserId(userId);
        chatUser.setIsConnected(true);
        chatUserDao.updateChatUser(chatUser);
    };


    public static void updateUserOfLine(Long userId){
        ChatUser chatUser = chatUserDao.findByUserId(userId);
        chatUser.setIsConnected(false);
        chatUser.setLastSeen(LocalDateTime.now());
        chatUserDao.updateChatUser(chatUser);
    };



}
