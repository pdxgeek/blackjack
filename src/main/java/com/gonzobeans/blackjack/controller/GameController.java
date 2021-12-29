package com.gonzobeans.blackjack.controller;

import com.gonzobeans.blackjack.dto.ClientMessage;
import com.gonzobeans.blackjack.dto.ServerMessage;
import com.gonzobeans.blackjack.model.Player;
import com.gonzobeans.blackjack.service.PlayerService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.Instant;

import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GameController {
    private final PlayerService playerService;

    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public ServerMessage chat(ClientMessage message) {
        messageTimeReporter(message);
        var playerName = playerService.getPlayer(message.getPlayerId())
                .map(Player::getName)
                .orElse("Unknown Player");

        return ServerMessage.builder()
                .content(playerName + ": " + escapeHtml4(message.getContent()))
                .build();
    }

    private void messageTimeReporter(ClientMessage message) {
        log.info("Provided time: {}, ReceivedTime: {}",  message.getTimestamp(), Instant.now());
    }
}
