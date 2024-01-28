package com.azu.chat.chat_collection.chat.chat_message.entity;


import com.azu.hospital.chat_collection.chat.entity.Chat;
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

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document
@Data
public class Message {

    @Id
    private String id;

    private String message;

    private MessageType type;

    private Long senderId;

    private Boolean isSeen;

    private Boolean isReceived;


    // TODO: 1/9/2024 For Reply

    private Boolean isReply = false;

    @DBRef
    private Message replyMessage;


    private String filePath;

    private String fileType;


    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @DBRef
    private Chat chat;


    public Message(String message, Long senderId) {
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
