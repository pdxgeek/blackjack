package com.gonzobeans.blackjack.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ChatMessage extends ClientMessage {
    private String content;
}
