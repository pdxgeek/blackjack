package com.gonzobeans.blackjack.model;

import lombok.Getter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static com.gonzobeans.blackjack.model.Card.Face.STANDARD_CARD_FACES;
import static com.gonzobeans.blackjack.model.Card.Suit.CARD_SUITS;


public class Shoe {
    private static final int SHUFFLE_DEPTH = 10;

    @Getter
    private final List<Card> cards;

    @Getter
    private final Deque<Card> discards;

    private final int deckCount;

    private Shoe(int deckCount) {
        this.cards = new ArrayList<>();
        this.discards = new ArrayDeque<>();
        this.deckCount = deckCount;
        shuffle();
    }

    public static Shoe ofStandardCards(int deckCount) {
        return new Shoe(deckCount);
    }

    public void shuffle() {
        cards.clear();
        discards.clear();
        cards.addAll(getDecks(deckCount));
        var random = ThreadLocalRandom.current();
        for (int x = 0; x < SHUFFLE_DEPTH; x++) {
            for (int i = 0; i < cards.size(); i++) {
                Collections.swap(cards, i, random.nextInt(cards.size() - 1));
            }
        }
    }

    public Optional<Card> draw() {
        if (!cards.isEmpty()) {
            var card = cards.get(0);
            cards.remove(0);
            return Optional.of(card);
        }
        return Optional.empty();
    }

    public void discard(Card card) {
        discards.push(card);
    }

    private static List<Card> getDecks(int count) {
        List<Card> deckCards = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            CARD_SUITS.forEach(suit -> STANDARD_CARD_FACES.forEach(face -> deckCards.add(Card.newCard(face, suit))));
        }
        return deckCards;
    }
}
