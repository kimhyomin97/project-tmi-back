package com.tmi.controller;

import com.tmi.dto.ChatMessage;
import com.tmi.kafka.ChatProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatProducer chatProducer;

    @PostMapping("/chat/send")
    public ResponseEntity<String> send(@RequestBody ChatMessage chatMessage) {
        chatProducer.sendMessage(chatMessage);
        return ResponseEntity.ok("Message sent");
    }
}
