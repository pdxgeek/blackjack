package com.gonzobeans.blackjack.controller;

import com.gonzobeans.blackjack.dto.ChatMessage;
import com.gonzobeans.blackjack.dto.ServerMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public ServerMessage chat(ChatMessage message) {
        return new ServerMessage();
    }
}
