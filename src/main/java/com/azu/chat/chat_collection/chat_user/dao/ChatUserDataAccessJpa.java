package com.azu.chat.chat_collection.chat_user.dao;

import com.azu.hospital.chat_collection.chat_user.entity.ChatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository("ChatUserDataAccessJpa")
public class ChatUserDataAccessJpa implements ChatUserDao {


    private final ChatUserRepository repository;

    @Autowired
    public ChatUserDataAccessJpa(ChatUserRepository repository) {
        this.repository = repository;
    }


    @Override
    public void createChatUser(ChatUser user) {
        repository.save(user);
    }

    @Override
    public void updateChatUser(ChatUser user) {
        repository.save(user);
    }

    @Override
    public ChatUser findByUserId(Long userId) {
        return repository.findAllByUserId(userId);
    }
}
