package com.azu.chat.chat_collection.chat_user.dao;

import com.azu.hospital.chat_collection.chat_user.entity.ChatUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatUserRepository extends MongoRepository<ChatUser , String> {


    ChatUser findAllByUserId(Long userId);

}
