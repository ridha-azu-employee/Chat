package com.azu.chat.chat_collection.general_chat.general_chat_messages.controllers;


import com.azu.hospital.chat_collection.general_chat.general_chat_messages.services.GeneralChatMessageService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/general-chat-messages")
public class GeneralMessageController {


    private final GeneralChatMessageService generalChatMessageService;

    public GeneralMessageController(GeneralChatMessageService generalChatMessageService) {
        this.generalChatMessageService = generalChatMessageService;
    }

    @GetMapping
    public ResponseEntity<?> getAllMessages(
            @PageableDefault Pageable pageable
            ) {
        return ResponseEntity.ok(generalChatMessageService.getGeneralChatMessages(pageable));
    }
}
