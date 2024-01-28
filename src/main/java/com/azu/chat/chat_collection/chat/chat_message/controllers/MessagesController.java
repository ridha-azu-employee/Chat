package com.azu.chat.chat_collection.chat.chat_message.controllers;

import com.azu.hospital.chat_collection.chat.chat_message.dto.MessageDto;
import com.azu.hospital.chat_collection.chat.chat_message.services.MessageServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/messages")
public class MessagesController {

    private final MessageServices messageServices;


    public MessagesController(MessageServices messageServices) {
        this.messageServices = messageServices;
    }


    @GetMapping("{chatId}")
    public ResponseEntity<Page<MessageDto>> getAllMessage(
            @PathVariable String chatId ,
            @PageableDefault Pageable pageable
            ){
        return ResponseEntity.ok(messageServices.getChatMessages(chatId , pageable));
    }
}
