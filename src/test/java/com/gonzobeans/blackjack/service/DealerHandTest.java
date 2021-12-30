package com.gonzobeans.blackjack.service;

import com.gonzobeans.blackjack.model.Card;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DealerHandTest {

    @Test
    void test_hidden_first_card() {
        var hand = new DealerHand();
        hand.addCard(Card.of(Card.Face.JACK, Card.Suit.HEART));
        hand.addCard(Card.of(Card.Face.ACE, Card.Suit.SPADE));
        assertEquals("Hidden", hand.displayCards().get(0));
        assertEquals(11, hand.calculateValue());
        hand.revealCards();
        assertEquals("Jack of Hearts", hand.displayCards().get(0));
        assertEquals(21, hand.calculateValue());
        assertTrue(hand.blackjack());
    }
}