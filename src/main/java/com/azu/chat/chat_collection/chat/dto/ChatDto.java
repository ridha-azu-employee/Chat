package com.azu.chat.chat_collection.chat.dto;

import com.azu.chat.chat_collection.chat.chat_message.dto.MessageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {

    private String id;

    private String chatId;

    private Long MessageFirstId;

    private Long MessageSecondId;

    private MessageDto lastMessage;

    private Long countUnReadMessages;

    private BaseUserDto chatWith;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;


}
