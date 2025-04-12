package com.tmi.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ChatConsumer {

    @KafkaListener(topics =  "chat-topic", groupId = "chat-consumer")
    public void receive(String jsonMessage) {
        System.out.println("Received message : " + jsonMessage);
    }
}
