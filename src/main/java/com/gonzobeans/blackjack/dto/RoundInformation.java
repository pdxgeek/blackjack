package com.gonzobeans.blackjack.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gonzobeans.blackjack.game.Round;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@JsonInclude(NON_NULL)
public class RoundInformation {
    private String id;
    private Round.RoundStatus roundStatus;
    private HandInformation dealerHand;
    private List<PlayerRoundInformation> players;
}
