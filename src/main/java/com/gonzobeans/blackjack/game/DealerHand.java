package com.gonzobeans.blackjack.game;

public class DealerHand extends Hand {
    public DealerHand() {
        super();
    }

    @Override
    public void addCard(Card card) {
        if (this.isEmpty()) {
            card.setHidden(true);
        }
        super.addCard(card);
    }

    public void revealCards() {
        getCards().forEach(card -> card.setHidden(false));
    }
}
