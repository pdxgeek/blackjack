package com.gonzobeans.blackjack.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PlayerRoundInformation {
    private String playerId;
    private List<HandInformation> hands;
}
