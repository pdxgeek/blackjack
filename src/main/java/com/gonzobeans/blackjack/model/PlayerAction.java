package com.gonzobeans.blackjack.model;

import lombok.Data;

@Data
public class PlayerAction {
    String playerId;
    String tableId;
    String roundId;
    Action action;
    int actionValue;
}
