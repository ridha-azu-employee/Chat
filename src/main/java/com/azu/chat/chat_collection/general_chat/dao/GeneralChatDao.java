package com.azu.chat.chat_collection.general_chat.dao;

import com.azu.hospital.chat_collection.general_chat.entity.GeneralChat;

import java.util.List;

public interface GeneralChatDao {
    GeneralChat createNewChat(GeneralChat chat);

    List<GeneralChat> getAll();

    Long countChat();

}
