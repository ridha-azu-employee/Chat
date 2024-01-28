package com.azu.chat.chat_collection.general_chat.general_chat_messages.dao;

import com.azu.hospital.chat_collection.general_chat.general_chat_messages.entity.GeneralChatMessages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("GeneralChatMessageDataAccessJpa")
public class GeneralChatMessageDataAccessJpa implements GeneralChatMessageDao {

    private final GeneralChatMessageRepository repository;

    public GeneralChatMessageDataAccessJpa(GeneralChatMessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<GeneralChatMessages> getMessageById(String id) {
        return repository.findById(id) ;
    }

    @Override
    public List<GeneralChatMessages> getNotSeenMessages(Long userId) {
        return repository.findAllBySeenByIdIsNot(userId);
    }

    @Override
    public Page<GeneralChatMessages> getChatMessages(Pageable pageable) {
        return repository.findAllByOrderByCreatedDateDesc(pageable);
    }

    @Override
    public GeneralChatMessages createNewMessage(GeneralChatMessages messages) {
        return repository.save(messages);
    }

    @Override
    public void updateAll(List<GeneralChatMessages> messages) {
        repository.saveAll(messages);
    }
}
