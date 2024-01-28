package com.azu.chat.chat_collection.chat.dao;

import com.azu.hospital.chat_collection.chat.entity.Chat;
import jakarta.transaction.Transactional;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Transactional
public interface ChatRepository extends MongoRepository<Chat , String> {


    Optional<Chat> findChatByChatId(String chatId);

    Optional<Chat> findChatById(String id);



    @Aggregation(pipeline = {
            "{ $lookup: { from: 'message', localField: '_id', foreignField: 'chat.$id', as: 'messages' } }",
            "{ $addFields: { lastMessage: { $arrayElemAt: ['$messages', -1] } } }",
            "{ $set: { 'lastMessage.createdDate': { $toString: '$lastMessage.createdDate' } } }",
            "{ $sort: { 'lastMessage.createdDate': -1 } }",
            "{ $match: { $expr: { $or: [ { $eq: ['$messageFirstId', ?0] }, { $eq: ['$messageSecondId', ?0] } ] } } }",
            "{ $skip: ?1 }",
            "{ $limit: ?2 }"

    })
    List<Chat> findAllChatsOrderedByLastMessageDate(@Param("userId") Long userId , @Param("offset") long offset, @Param(
            "limit") int limit);


    List<Chat> findAllByMessageFirstIdOrMessageSecondId(Long userId , Long userId2);

    Integer countAllByMessageFirstIdEqualsOrMessageSecondId(Long messageFirstId, Long messageSecondId);

}
