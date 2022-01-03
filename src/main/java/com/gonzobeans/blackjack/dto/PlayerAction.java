package com.gonzobeans.blackjack.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gonzobeans.blackjack.model.Action;
import com.gonzobeans.blackjack.model.Player;
import lombok.Data;

@Data
public class PlayerAction {
    String playerId;
    String tableId;
    String roundId;
    String handId;
    Action action;
    int actionValue;

    @JsonIgnore
    Player player;
}
