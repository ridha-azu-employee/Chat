package com.azu.chat.chat_collection.general_chat.general_chat_messages.dao;

import com.azu.hospital.chat_collection.general_chat.general_chat_messages.entity.GeneralChatMessages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GeneralChatMessageRepository extends MongoRepository<GeneralChatMessages, String> {



    List<GeneralChatMessages> findAllBySeenByIdIsNot(Long userId);




    Page<GeneralChatMessages> findAllByOrderByCreatedDateDesc(Pageable pageable);



}
