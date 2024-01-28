package com.azu.chat.chat_collection.general_chat.dao;

import com.azu.hospital.chat_collection.general_chat.entity.GeneralChat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GeneralChatRepository extends MongoRepository<GeneralChat , String> {


}
