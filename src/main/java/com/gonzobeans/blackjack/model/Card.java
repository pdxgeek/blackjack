package com.gonzobeans.blackjack.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode
@RequiredArgsConstructor
public class Card implements Comparable<Card> {
    private final Face face;
    private final Suit suit;
    private final Color color;

    @Setter
    private boolean hidden = false;

    private Card(Face face, Suit suit) {
        this.face = face;
        this.suit = suit;
        this.color = suit.getColor();
    }

    public static Card newCard(Face face, Suit suit) {
        return new Card(face, suit);
    }

    public static Card newJoker(Color color) {
        return new Card(Face.JOKER, Suit.NONE, color);
    }

    public Face getFace() {
        return hidden ? Face.HIDDEN : face;
    }

    public Suit getSuit() {
        return hidden ? Suit.HIDDEN : suit;
    }

    public String peek() {
        return face + " of " + suit;
    }

    public int pointValue() {
        return getFace().getPointValue();
    }

    @Override
    public int compareTo(Card o) {
        return Integer.compare(this.getFace().getComparisonValue(), o.getFace().getComparisonValue());
    }

    @Override
    public String toString() {
        return getFace() + (getSuit() == Suit.HIDDEN ? "" :  " of " + getSuit());
    }


    @Getter
    @RequiredArgsConstructor
    public enum Color {
        RED("Red"), BLACK("Black"), NONE(""), HIDDEN("Hidden");

        private final String name;

        @Override
        public String toString() {
            return name;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum Suit {
        HEARTS("Hearts", Color.RED),
        CLUBS("Clubs", Color.BLACK),
        DIAMONDS("Diamonds", Color.RED),
        SPADES("Spades", Color.BLACK),
        NONE("None", Color.NONE),
        HIDDEN("Hidden", Color.NONE);

        public static final List<Suit> CARD_SUITS =
                List.of(CLUBS, DIAMONDS, HEARTS, SPADES);

        private final String name;
        private final Color color;

        @Override
        public String toString() {
            return name;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum Face {
        ACE("Ace", 1, 14, "A"),
        TWO("Two", 2,2, "2"),
        THREE("Three", 3,3, "3"),
        FOUR("Four", 4,4, "4"),
        FIVE("Five", 5,5, "5"),
        SIX("Six", 6,6, "6"),
        SEVEN("Seven", 7,7, "7"),
        EIGHT("Eight", 8,8, "8"),
        NINE("Nine", 9,9, "9"),
        TEN("Ten", 10,10, "10"),
        JACK("Jack", 10, 11, "J"),
        QUEEN("Queen", 10, 12, "Q"),
        KING("King", 10,13, "K"),
        JOKER("Joker", 0,14, "W"),
        HIDDEN("Hidden", 0,0, "?");

        public static final List<Face> STANDARD_CARD_FACES =
                List.of(ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING);

        public static final List<Face> CARD_FACES_WITH_JOKER =
                List.of(ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, JOKER);

        private final String name;
        private final int pointValue;
        private final int comparisonValue;
        private final String symbol;

        @Override
        public String toString() {
            return name;
        }
    }
}
