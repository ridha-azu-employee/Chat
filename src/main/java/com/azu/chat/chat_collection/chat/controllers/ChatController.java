package com.azu.chat.chat_collection.chat.controllers;


import com.azu.hospital.chat_collection.chat.services.ChatService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/chats")
public class ChatController {

    private final ChatService chatService;


    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }


    @GetMapping
    public ResponseEntity<?> getAllChats(
            @PageableDefault Pageable pageable
            ){
        return ResponseEntity.ok(chatService.getAllChats(pageable));
    }
}
