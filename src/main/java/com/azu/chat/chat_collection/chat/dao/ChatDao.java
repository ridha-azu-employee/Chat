package com.azu.chat.chat_collection.chat.dao;

import com.azu.hospital.chat_collection.chat.entity.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatDao {


    Optional<Chat> findByChatId(String chatId);

    Optional<Chat> findById(String id);

    void updateChat(Chat chat);

    Chat createNewChat(Chat chat);

    List<Chat> findAllChatsByUserId(Long userId);
    List<Chat> findAllSortedByMaxMessageCreatedAt(Long userId ,  long offset,  int limit);

    Integer countAllByUserId(Long userId1 );

}
