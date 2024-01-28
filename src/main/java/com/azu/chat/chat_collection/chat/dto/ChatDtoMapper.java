package com.azu.chat.chat_collection.chat.dto;

import com.azu.hospital.chat_collection.chat.chat_message.dto.MessageDtoMapper;
import com.azu.hospital.chat_collection.chat.chat_message.entity.Message;
import com.azu.hospital.chat_collection.chat.entity.Chat;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.function.Function;

@Service
public class ChatDtoMapper implements Function<Chat, ChatDto> {

    private final MessageDtoMapper mapper;

    public ChatDtoMapper(MessageDtoMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public ChatDto apply(Chat chat) {
        return new ChatDto(
                chat.getId(),
                chat.getChatId(),
                chat.getMessageFirstId(),
                chat.getMessageSecondId(),
                chat.getMessages().isEmpty() ? null :
                        mapper.apply(chat.getMessages()
                                .stream().max(Comparator.comparing(Message::getCreatedDate)).get()),
                chat.getMessages().isEmpty() ? null : chat.getMessages().stream().filter(m -> !m.getIsSeen()).count(),
                null,
                chat.getCreatedDate(),
                chat.getLastModifiedDate()
        );
    }
}
