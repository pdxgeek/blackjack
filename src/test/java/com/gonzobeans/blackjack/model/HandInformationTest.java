package com.gonzobeans.blackjack.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gonzobeans.blackjack.dto.HandInformation;
import com.gonzobeans.blackjack.game.Card;
import com.gonzobeans.blackjack.game.PlayerHand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


class HandInformationTest {
    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Test
    void printHandInformation() throws JsonProcessingException {
        var cards = List.of(Card.newCard(Card.Face.ACE, Card.Suit.SPADES),
                        Card.newCard(Card.Face.HIDDEN, Card.Suit.HIDDEN));
        var info = HandInformation.builder()
                .cards(cards.stream().map(Card::toCode).toList())
                .status(PlayerHand.HandStatus.WAITING_FOR_INSURANCE)
                .handValue(21)
                .build();
        System.out.println(mapper.writeValueAsString(info));
    }
}