package com.azu.chat.chat_collection.general_chat.general_chat_messages.dto;

import com.azu.hospital.chat_collection.general_chat.general_chat_messages.entity.GeneralChatMessages;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class GeneralMessageDtoMapper implements Function<GeneralChatMessages ,GeneralMessageDto > {
    @Override
    public GeneralMessageDto apply(GeneralChatMessages messages) {
        return new GeneralMessageDto(
                messages.getId(),
                messages.getMessage(),
                messages.getType(),
                false,
                messages.getIsReply(),
                messages.getReplyMessage() == null ? null : this.apply(messages.getReplyMessage()),
                messages.getFilePath(),
                messages.getFileType(),
                messages.getCreatedDate().toString()
        );
    }
}
