package com.azu.chat.chat_collection.general_chat.general_chat_messages.dto;

import com.azu.hospital.utils.enums.chat.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralMessageDto {

    private String id;

    private String message;

    private MessageType type;

    private Boolean isSender;

    private Boolean isReply ;

    private GeneralMessageDto replyMessageId;

    private String filePath;

    private String fileType;

    private String createdDate;




}
