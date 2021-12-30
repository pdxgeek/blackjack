package com.gonzobeans.blackjack.service;

import com.gonzobeans.blackjack.exception.BlackJackRulesException;
import com.gonzobeans.blackjack.model.Card;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gonzobeans.blackjack.model.Card.Face.ACE;

@Getter
public abstract class Hand {

    private final List<Card> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public List<String> displayCards() {
        return cards.stream().map(Card::toString).collect(Collectors.toList());
    }

    public boolean blackjack() {
        return (cards.size() == 2 && calculateValue() == 21);
    }

    public boolean isEmpty() {
        return cards.size() == 0;
    }

    public int size() {
        return cards.size();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public int calculateValue() {
        if (cards.size() < 2) {
            throw new BlackJackRulesException("Cannot calculate value with less than 2 cards.");
        }
        var value = cards.stream()
                .mapToInt(Card::pointValue)
                .sum();

        return (value <= 11 && cards.stream().anyMatch(card -> card.getFace().equals(ACE)))
                ? value + 10
                :value;
    }
}
