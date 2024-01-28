package com.azu.chat.chat_collection.general_chat.dao;

import com.azu.hospital.chat_collection.general_chat.entity.GeneralChat;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("GeneralChatDataAccessJpa")
public class GeneralChatDataAccessJpa implements GeneralChatDao {

    private final GeneralChatRepository repository;


    public GeneralChatDataAccessJpa(GeneralChatRepository repository) {
        this.repository = repository;
    }

    @Override
    public GeneralChat createNewChat(GeneralChat chat) {
        return repository.save(chat);
    }

    @Override
    public List<GeneralChat> getAll() {
        return repository.findAll();
    }

    @Override
    public Long countChat() {
        return repository.count();
    }
}
