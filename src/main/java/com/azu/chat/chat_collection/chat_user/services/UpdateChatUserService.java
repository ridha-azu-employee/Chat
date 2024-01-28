package com.azu.chat.chat_collection.chat_user.services;

import com.azu.hospital.chat_collection.chat_user.dao.ChatUserDao;
import com.azu.hospital.chat_collection.chat_user.entity.ChatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("UpdateChatUserService")
public class UpdateChatUserService implements IUpdateChatUserService{


    private final ChatUserDao chatUserDao;

    @Autowired
    public UpdateChatUserService(@Qualifier("ChatUserDataAccessJpa") ChatUserDao chatUserDao) {
        this.chatUserDao = chatUserDao;
    }


    @Override
    public void updateUser(ChatUser chatUser) {
        chatUserDao.updateChatUser(chatUser);
    }
}
