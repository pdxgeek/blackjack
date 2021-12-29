package com.gonzobeans.blackjack.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@SuperBuilder
public class ClientMessage implements Message {
    private final Instant timestamp = Instant.now();
    protected String clientId;
}
