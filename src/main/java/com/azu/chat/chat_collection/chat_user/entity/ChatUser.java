package com.azu.chat.chat_collection.chat_user.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document
@Data
public class ChatUser {

    @Id
    private String id;

    private Long userId;

    private String email;

    private String name;

    private String image;

    private String mobile;

    private Boolean isConnected;

    private LocalDateTime lastSeen;

    public ChatUser(
            Long userId,
            String email,
            String name,
            String image,
            String mobile
    ) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.image = image;
        this.mobile = mobile;

    }



}
