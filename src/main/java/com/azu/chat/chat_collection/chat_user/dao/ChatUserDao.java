package com.azu.chat.chat_collection.chat_user.dao;

import com.azu.hospital.chat_collection.chat_user.entity.ChatUser;

public interface ChatUserDao {

    void createChatUser(ChatUser user);
    void updateChatUser(ChatUser user);

    ChatUser findByUserId(Long userId);


}
