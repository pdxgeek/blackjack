package com.gonzobeans.blackjack.service;

import com.gonzobeans.blackjack.game.DealerHand;
import org.junit.jupiter.api.Test;

import static com.gonzobeans.blackjack.game.Card.Face.ACE;
import static com.gonzobeans.blackjack.game.Card.Face.JACK;
import static com.gonzobeans.blackjack.game.Card.Suit.HEARTS;
import static com.gonzobeans.blackjack.game.Card.Suit.SPADES;
import static com.gonzobeans.blackjack.game.Card.newCard;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DealerHandTest {

    @Test
    void test_hidden_first_card() {
        var hand = new DealerHand();
        hand.addCard(newCard(JACK, HEARTS));
        hand.addCard(newCard(ACE, SPADES));
        assertEquals("Hidden", hand.displayCards().get(0));
        assertEquals(11, hand.calculateValue());
        hand.revealCards();
        assertEquals("Jack of Hearts", hand.displayCards().get(0));
        assertEquals(21, hand.calculateValue());
        assertTrue(hand.blackjack());
    }
}