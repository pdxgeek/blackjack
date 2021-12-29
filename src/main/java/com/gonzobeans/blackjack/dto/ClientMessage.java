package com.gonzobeans.blackjack.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@SuperBuilder
public class ClientMessage implements Message {
    private Instant timestamp;
    private String playerId;
    private String playerKey;
    private String content;
}
