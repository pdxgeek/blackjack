package com.gonzobeans.blackjack.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class ServerMessage implements Message {
    private final Instant timestamp = Instant.now();
}
