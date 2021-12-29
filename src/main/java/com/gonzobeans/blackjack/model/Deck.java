package com.gonzobeans.blackjack.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static com.gonzobeans.blackjack.model.Card.Face;
import static com.gonzobeans.blackjack.model.Card.Face.ACE;
import static com.gonzobeans.blackjack.model.Card.Face.EIGHT;
import static com.gonzobeans.blackjack.model.Card.Face.FIVE;
import static com.gonzobeans.blackjack.model.Card.Face.FOUR;
import static com.gonzobeans.blackjack.model.Card.Face.JACK;
import static com.gonzobeans.blackjack.model.Card.Face.KING;
import static com.gonzobeans.blackjack.model.Card.Face.NINE;
import static com.gonzobeans.blackjack.model.Card.Face.QUEEN;
import static com.gonzobeans.blackjack.model.Card.Face.SEVEN;
import static com.gonzobeans.blackjack.model.Card.Face.SIX;
import static com.gonzobeans.blackjack.model.Card.Face.TEN;
import static com.gonzobeans.blackjack.model.Card.Face.THREE;
import static com.gonzobeans.blackjack.model.Card.Face.TWO;
import static com.gonzobeans.blackjack.model.Card.Suit;
import static com.gonzobeans.blackjack.model.Card.Suit.CLUB;
import static com.gonzobeans.blackjack.model.Card.Suit.DIAMOND;
import static com.gonzobeans.blackjack.model.Card.Suit.HEART;
import static com.gonzobeans.blackjack.model.Card.Suit.SPADE;


public class Deck {
    private static final int SHUFFLE_DEPTH = 10;
    private static final List<Face> CARD_FACES =
            List.of(ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING);
    private static final List<Suit> CARD_SUITS =
            List.of(CLUB, DIAMOND, HEART, SPADE);

    @Getter
    private final List<Card> cards;
    @Getter
    private final List<Card> discards;


    private Deck(Collection<Card> cards) {
        this.cards = new ArrayList<>(cards);
        this.discards = new ArrayList<>();
    }

    public static Deck ofStandardCards() {
        return ofStandardCards(1);
    }

    public static Deck ofStandardCards(int shoeCount) {
        List<Card> deckCards = new ArrayList<>();
        for(int i = 0; i < shoeCount; i++) {
            CARD_SUITS.forEach(suit -> CARD_FACES.forEach(face -> deckCards.add(Card.of(face, suit))));
        }
        return new Deck(deckCards);
    }

    public void shuffle() {
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
        discards.add(card);
    }
}
