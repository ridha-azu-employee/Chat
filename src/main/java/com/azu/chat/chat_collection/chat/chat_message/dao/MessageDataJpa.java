package com.azu.chat.chat_collection.chat.chat_message.dao;

import com.azu.hospital.chat_collection.chat.chat_message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("MessageDataJpa")
public class MessageDataJpa implements MessageDao {

    private final MessageRepository chatRepository;

    public MessageDataJpa(MessageRepository chatRepository) {
        this.chatRepository = chatRepository;
    }


    @Override
    public Message createNewMessage(Message message) {
        return chatRepository.save(message);
    }

    @Override
    public Optional<Message> getMessageById(String messageId) {
        return chatRepository.findById(messageId);
    }

    @Override
    public Page<Message> getChatMessages(String chatId , Pageable pageable) {
        return chatRepository.findAllByChatIdOrderByCreatedDateDesc(chatId , pageable);
    }

    @Override
    public void updateListOfMessage(List<Message> messages) {
        chatRepository.saveAll(new ArrayList<>(messages));
    }
}
