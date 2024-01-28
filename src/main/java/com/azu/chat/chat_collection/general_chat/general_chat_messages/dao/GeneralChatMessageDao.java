package com.azu.chat.chat_collection.general_chat.general_chat_messages.dao;

import com.azu.hospital.chat_collection.general_chat.general_chat_messages.entity.GeneralChatMessages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface GeneralChatMessageDao {


    Optional<GeneralChatMessages> getMessageById(String id);

    List<GeneralChatMessages> getNotSeenMessages(Long userId);


    Page<GeneralChatMessages> getChatMessages(Pageable pageable);

    GeneralChatMessages createNewMessage(GeneralChatMessages messages);

    void updateAll(List<GeneralChatMessages> messages);
}
