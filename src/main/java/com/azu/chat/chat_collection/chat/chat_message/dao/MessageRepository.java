package com.azu.chat.chat_collection.chat.chat_message.dao;

import com.azu.hospital.chat_collection.chat.chat_message.entity.Message;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

@Transactional
public interface MessageRepository extends MongoRepository<Message, String> {


    Page<Message> findAllByChatIdOrderByCreatedDateDesc(String chatId , Pageable pageable);


}
