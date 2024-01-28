package com.azu.chat.chat_collection.general_chat.general_chat_messages.entity;

import com.azu.hospital.chat_collection.chat_user.entity.ChatUser;
import com.azu.hospital.chat_collection.general_chat.entity.GeneralChat;
import com.azu.hospital.utils.enums.chat.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document
@Data
public class GeneralChatMessages {


    @Id
    private String id;

    private String message;

    private MessageType type;

    private Long senderId;


    private Boolean isReply = false;

    @DBRef
    private GeneralChatMessages replyMessage;


    @DBRef
    private List<ChatUser> seenBy = new ArrayList<>();


    private String filePath;

    private String fileType;


    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @DBRef
    private GeneralChat chat;


    public GeneralChatMessages(String message, Long senderId) {
        this.message = message;
        this.senderId = senderId;

    }


    public String getCreatedDate() {
        return createdDate.toString();
    }

    public String getLastModifiedDate() {
        return lastModifiedDate.toString();
    }


}
