package com.azu.chat.chat_collection.chat.chat_message.dto;

import com.azu.hospital.chat_collection.chat.chat_message.entity.Message;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class MessageDtoMapper implements Function<Message, MessageDto> {

    @Override
    public MessageDto apply(Message message) {
        return new MessageDto(
                message.getId(),
                message.getMessage(),
                message.getType(),
                false,
                message.getIsSeen(),
                message.getIsReceived(),
                message.getFilePath() == null ? null : message.getFilePath(),
                message.getFileType() == null ? null : message.getFileType(),
                message.getCreatedDate(),
                message.getLastModifiedDate(),
                message.getChat().getId(),
                message.getIsReply() == null ? null : message.getIsReply(),
                message.getReplyMessage() == null ? null : this.apply(message.getReplyMessage())
        );
    }
}
