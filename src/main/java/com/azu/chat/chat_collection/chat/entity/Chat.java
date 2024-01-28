package com.azu.chat.chat_collection.chat.entity;

import com.azu.chat.chat_collection.chat.chat_message.entity.Message;
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
public class Chat {

    @Id
    private String id;

    private String chatId;

    private Long messageFirstId;

    private Long messageSecondId;

    @DBRef(lazy = true)
    private List<Message> messages = new ArrayList<>();


    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    public Chat(String chatId) {
        this.chatId = chatId;
    }
}
