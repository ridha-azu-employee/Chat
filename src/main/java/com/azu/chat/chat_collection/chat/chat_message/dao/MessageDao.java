package com.azu.chat.chat_collection.chat.chat_message.dao;

import com.azu.hospital.chat_collection.chat.chat_message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MessageDao {

    Message createNewMessage(Message message);

    Optional<Message> getMessageById(String messageId);

    Page<Message> getChatMessages(String chatId , Pageable pageable);


    void updateListOfMessage(List<Message> messages);

}
