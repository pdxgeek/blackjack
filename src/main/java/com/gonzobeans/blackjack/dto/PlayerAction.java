package com.gonzobeans.blackjack.dto;

import com.gonzobeans.blackjack.model.Action;
import lombok.Data;

@Data
public class PlayerAction {
    String playerId;
    String tableId;
    String roundId;
    String handId;
    Action action;
    int actionValue;
}
