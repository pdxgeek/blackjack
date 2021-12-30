package com.gonzobeans.blackjack.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class Card implements Comparable<Card> {
    private final Face face;
    private final Suit suit;
    private final Color color;

    @Setter
    // Hidden cards may not be seen by other players
    private boolean hidden = false;

    private Card(Face face, Suit suit) {
        this.face = face;
        this.suit = suit;
        this.color = suit.getColor();
    }

    public Face getFace() {
        return hidden ? Face.HIDDEN : face;
    }

    public Suit getSuit() {
        return hidden ? Suit.HIDDEN : suit;
    }

    public static Card of(Face face, Suit suit) {
        return new Card(face, suit);
    }

    public static Card jokerOf(Color color) {
        return new Card(Face.JOKER, Suit.NONE, color);
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
        return face + " of " + suit;
    }

    public String display() {
        return getFace() + ((getSuit() == Suit.HIDDEN) ? "" :  " of " + getSuit());
    }

    public enum Color {
        RED, BLACK, NONE
    }

    @Getter
    @RequiredArgsConstructor
    public enum Suit {
        HEART("Hearts", Color.RED),
        CLUB("Clubs", Color.BLACK),
        DIAMOND("Diamonds", Color.RED),
        SPADE("Spades", Color.BLACK),
        NONE("None", Color.NONE),
        HIDDEN("Hidden", Color.NONE);

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
