package com.azu.chat.chat_collection.chat.services;

import com.azu.hospital.all_user_sevices.user_utils.base_user_service.BaseUserDao;
import com.azu.hospital.all_user_sevices.user_utils.base_user_service.dto.BaseUserDtoMapper;
import com.azu.hospital.chat_collection.chat.dao.ChatDao;
import com.azu.hospital.chat_collection.chat.dto.ChatDto;
import com.azu.hospital.chat_collection.chat.dto.ChatDtoMapper;
import com.azu.hospital.chat_collection.chat.entity.Chat;
import com.azu.hospital.security.newsecurity.config.JwtService;
import com.azu.hospital.utils.Generic.GenericBaseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService extends GenericBaseService {

    private static ChatDao chatDao;

    private final ChatDtoMapper mapper;

    private final BaseUserDtoMapper userDtoMapper;

    private final BaseUserDao userDao;


    public ChatService(
            @Qualifier("ChatDataJpa") ChatDao chatDao,
            ChatDtoMapper mapper,
            JwtService jwtService,
            HttpServletRequest httpServletRequest,
            BaseUserDtoMapper userDtoMapper,
            BaseUserDao userDao) {
        super(jwtService, httpServletRequest);
        this.chatDao = chatDao;
        this.mapper = mapper;
        this.userDtoMapper = userDtoMapper;
        this.userDao = userDao;
    }


    public static Chat checkChat(Long fromUser, Long toUser) {


        Optional<Chat> chat1 = chatDao.findByChatId(fromUser + "-" + toUser);
        Optional<Chat> chat2 = chatDao.findByChatId(toUser + "-" + fromUser);


        if (chat1.isEmpty() && chat2.isEmpty()) {
            Chat chat = new Chat(fromUser + "-" + toUser);
            chat.setMessageFirstId(fromUser);
            chat.setMessageSecondId(toUser);
            return chatDao.createNewChat(chat);
        } else {
            return chat1.orElseGet(chat2::get);
        }
    }

    public static Chat getChatByChatId(String chatId) {
        return chatDao.findById(chatId).orElse(null);
    }


    public Page<ChatDto> getAllChats(Pageable pageable) {

        List<ChatDto> chatDtos =
                chatDao.findAllSortedByMaxMessageCreatedAt(authId(), pageable.getOffset(), pageable.getPageSize()).stream().map(c -> {
                    ChatDto chatDto = mapper.apply(c);

                    if (c.getMessageFirstId() == authId()) {
                        chatDto.setChatWith(userDtoMapper.apply(Objects.requireNonNull(userDao.findById(c.getMessageSecondId()).orElse(null))));
                    } else {
                        chatDto.setChatWith(userDtoMapper.apply(Objects.requireNonNull(userDao.findById(c.getMessageFirstId()).orElse(null))));
                    }

                    return chatDto;
                }).collect(Collectors.toList());

        Integer totalCount = chatDao.countAllByUserId(authId());


        return new PageImpl<>(chatDtos, pageable, totalCount);
    }


}
