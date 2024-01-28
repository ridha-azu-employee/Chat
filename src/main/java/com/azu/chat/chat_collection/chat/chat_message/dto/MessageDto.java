package com.azu.chat.chat_collection.chat.chat_message.dto;

import com.azu.hospital.utils.enums.chat.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageDto {

    private String id;

    private String message;

    private MessageType type;

    private Boolean isSender ;

    private Boolean isSeen;

    private Boolean isReceived;

    private String filePath;

    private String fileType;

    private String createdDate;

    private String lastModifiedDate;

    private String chatId;

    private Boolean isReply;

    private MessageDto replayedMessage;


}
