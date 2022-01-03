package com.gonzobeans.blackjack.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gonzobeans.blackjack.game.PlayerHand;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@JsonInclude(NON_NULL)
public class HandInformation {
    private String id;
    private PlayerHand.HandStatus status;
    private PlayerHand.Insurance insurance;
    private Integer bet;
    private List<String> cards;
    private Integer handValue;
}
