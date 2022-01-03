package com.gonzobeans.blackjack.game;

import com.gonzobeans.blackjack.exception.BlackJackRulesException;
import com.gonzobeans.blackjack.dto.HandInformation;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gonzobeans.blackjack.game.Card.Face.ACE;

@Getter
public abstract class Hand {
    private static final int TWENTY_ONE = 21;
    private final String id;
    private final List<Card> cards;

    public Hand() {
        this.cards = new ArrayList<>();
        this.id = UUID.randomUUID().toString();
    }

    public List<String> displayCards() {
        return cards.stream().map(Card::toString).collect(Collectors.toList());
    }

    public boolean blackjack() {
        return (cards.size() == 2 && calculateValue() == TWENTY_ONE);
    }

    public boolean busted() {
        return calculateValue() > TWENTY_ONE;
    }

    public boolean notBusted() {
        return calculateValue() <= TWENTY_ONE;
    }

    public boolean isEmpty() {
        return cards.size() == 0;
    }

    public boolean isPlayable() {
        return calculateValue() < 21;
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
                : value;
    }

    public HandInformation getInfo() {
        return HandInformation.builder()
                .cards(getCards().stream().map(Card::toCode).toList())
                .handValue(calculateValue() > 0 ? calculateValue() : null)
                .build();
    }
}
