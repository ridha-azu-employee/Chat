package com.azu.chat.chat_collection.general_chat.entity;


import com.azu.hospital.chat_collection.general_chat.general_chat_messages.entity.GeneralChatMessages;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
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
public class GeneralChat {


    @Id
    private String id;


    @DBRef(lazy = true)
    private List<GeneralChatMessages> messages = new ArrayList<>();


    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

}
