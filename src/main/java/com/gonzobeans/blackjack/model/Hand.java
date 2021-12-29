package com.gonzobeans.blackjack.model;

import com.gonzobeans.blackjack.exception.BlackJackRulesException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gonzobeans.blackjack.model.Card.Face.ACE;

@Getter
public class Hand {
    private String id;
    private List<Card> cards;
    private int bet;
    private boolean isSplit;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public Hand(int bet) {
        this.cards = new ArrayList<>();
        this.bet = bet;
    }

    public Hand(Card card, int bet) {
        this.id = UUID.randomUUID().toString();
        this.cards = new ArrayList<>(List.of(card));
        this.bet = bet;
        this.isSplit = true;
    }

    public boolean blackjack() {
        return (cards.size() == 2 && calculateValue() == 21);
    }

    public boolean canSplit() {
        // TODO: Add splitting logic
        return false;
    }

    public Hand split() {
        if (cards.size() == 2) {
            var splitCard = cards.get(1);
            cards.remove(1);
            return new Hand(splitCard, bet);
        }
        throw new BlackJackRulesException("Must have exactly 2 cards in hand to split");
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public int calculateValue() {
        var value = cards.stream()
                .mapToInt(Card::pointValue)
                .sum();

        return (value <= 11 && cards.stream().anyMatch(card -> card.getFace().equals(ACE)))
                ? value + 10
                :value;
    }
}
