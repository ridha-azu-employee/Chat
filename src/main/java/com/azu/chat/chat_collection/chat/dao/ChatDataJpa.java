package com.azu.chat.chat_collection.chat.dao;

import com.azu.hospital.chat_collection.chat.entity.Chat;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("ChatDataJpa")
public class ChatDataJpa implements ChatDao{

    private final ChatRepository chatRepository;

    public ChatDataJpa(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public Optional<Chat> findByChatId(String chatId) {
        return chatRepository.findChatByChatId(chatId);
    }

    @Override
    public Optional<Chat> findById(String id) {
        return chatRepository.findById(id);
    }

    @Override
    public void updateChat(Chat chat) {
        chatRepository.save(chat);
    }

    @Override
    public Chat createNewChat(Chat chat) {
        return chatRepository.save(chat);
    }

    @Override
    public List<Chat> findAllChatsByUserId(Long userId) {
        return chatRepository.findAllByMessageFirstIdOrMessageSecondId(userId, userId);
    }

    @Override
    public List<Chat> findAllSortedByMaxMessageCreatedAt(Long userId  ,  long offset,  int limit) {
        return chatRepository.findAllChatsOrderedByLastMessageDate(userId ,  offset,  limit);
    }

    @Override
    public Integer countAllByUserId(Long userId1) {
        return chatRepository.countAllByMessageFirstIdEqualsOrMessageSecondId(userId1 , userId1);
    }
}
