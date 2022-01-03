package com.gonzobeans.blackjack.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TableInformation {
    private String id;
    private String name;
    private int emptySeats;
    private int maximumSeats;
    private int minimumBet;
}
