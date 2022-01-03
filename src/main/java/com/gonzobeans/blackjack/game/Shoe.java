package com.gonzobeans.blackjack.game;

import com.gonzobeans.blackjack.exception.NoCardsAvailableException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static com.gonzobeans.blackjack.game.Card.Face.STANDARD_CARD_FACES;
import static com.gonzobeans.blackjack.game.Card.Suit.CARD_SUITS;


public class Shoe {
    private static final int SHUFFLE_DEPTH = 10;
    private static final int MINIMUM_RESHUFFLE = 52;

    @Getter
    private final List<Card> cards;

    private int marker;

    private final int deckCount;

    private Shoe(int deckCount) {
        this.cards = new ArrayList<>();
        this.deckCount = deckCount;
        shuffle();
    }

    public static Shoe ofStandardCards(int deckCount) {
        return new Shoe(deckCount);
    }

    public void startNewGame() {
        if (cards.size() < marker || cards.size() < MINIMUM_RESHUFFLE) {
            shuffle();
            // TODO: Notify Table of Shuffle
        }
    }

    public void shuffle() {
        cards.clear();
        cards.addAll(getDecks(deckCount));
        var random = ThreadLocalRandom.current();
        for (int x = 0; x < SHUFFLE_DEPTH; x++) {
            for (int i = 0; i < cards.size(); i++) {
                Collections.swap(cards, i, random.nextInt(cards.size() - 1));
            }
        }
        marker = ThreadLocalRandom.current().nextInt(cards.size() / 4, cards.size() / 2);
    }

    public Card draw() {
        return getCard().orElseThrow(() -> new NoCardsAvailableException("No cards left in shoe."));
    }

    public Optional<Card> getCard() {
        if (!cards.isEmpty()) {
            var card = cards.get(0);
            cards.remove(0);
            return Optional.of(card);
        }
        return Optional.empty();
    }

    private static List<Card> getDecks(int count) {
        List<Card> deckCards = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            CARD_SUITS.forEach(suit -> STANDARD_CARD_FACES.forEach(face -> deckCards.add(Card.newCard(face, suit))));
        }
        return deckCards;
    }
}
