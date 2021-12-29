package com.gonzobeans.blackjack.dto;

import java.time.Instant;

public interface Message {
    Instant getTimestamp();
    String getContent();
}
