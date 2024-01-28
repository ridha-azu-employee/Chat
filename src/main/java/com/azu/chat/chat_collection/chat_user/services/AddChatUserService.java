package com.azu.chat.chat_collection.chat_user.services;

import com.azu.hospital.chat_collection.chat_user.dao.ChatUserDao;
import com.azu.hospital.chat_collection.chat_user.entity.ChatUser;
import com.azu.hospital.chat_collection.chat_user.request.ChatUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("AddChatUserService")
public class AddChatUserService implements IAddChatUserService{


    private final ChatUserDao chatUserDao;

    @Autowired
    public AddChatUserService(@Qualifier("ChatUserDataAccessJpa") ChatUserDao chatUserDao) {
        this.chatUserDao = chatUserDao;
    }


    public void createUser(ChatUserRequest request){

        ChatUser chatUser= new ChatUser(
                request.userId(),
                request.email(),
                request.name(),
                request.image(),
                request.mobile()
        );

        chatUserDao.createChatUser(chatUser);

    }


}

