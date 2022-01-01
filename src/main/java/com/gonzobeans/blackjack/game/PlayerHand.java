package com.gonzobeans.blackjack.game;

import com.gonzobeans.blackjack.exception.BlackJackRulesException;
import com.gonzobeans.blackjack.exception.NoCardsAvailableException;
import com.gonzobeans.blackjack.model.Card;
import com.gonzobeans.blackjack.model.Player;
import com.gonzobeans.blackjack.model.Shoe;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class PlayerHand extends Hand {
    private int bet;
    private Action nextAction;
    private boolean handOpen;
    @Getter(AccessLevel.NONE)
    private boolean insurance;

    public PlayerHand(int bet) {
        super();
        this.bet = bet;
        this.handOpen = true;
        this.insurance = false;
        nextAction = Action.NONE_SELECTED;
    }

    @Override
    public boolean isPlayable() {
        return super.isPlayable() && handOpen;
    }

    public void hit(Shoe shoe) {
        if (!isPlayable()) {
            throw new BlackJackRulesException("Hand is not playable.");
        }
        addCard(shoe.draw().orElseThrow(() -> {
            nextAction = Action.NOT_AVAILABLE;
            return new NoCardsAvailableException("No cards left in shoe.");
        }));
        nextAction = isPlayable()
                ? Action.NONE_SELECTED
                : Action.NOT_AVAILABLE;
    }

    public boolean canDoubleDown(Player player) {
        return player.getMoney() >= bet
                && handOpen
                && size() == 2
                && calculateValue() >= 9
                && calculateValue() <= 11;
    }

    public void doubleDown(Player player, Shoe shoe) {
        if (!canDoubleDown(player)) {
            nextAction = Action.NONE_SELECTED;
            throw new BlackJackRulesException("Hand is not eligible to double down");
        }
        bet = bet + getMoneyFromPlayer(player, bet);
        addCard(getCardFromShoe(shoe));
        this.handOpen = false;
        nextAction = Action.NOT_AVAILABLE;
    }

    public void stay() {
        this.handOpen = false;
        nextAction = Action.NOT_AVAILABLE;
    }

    public boolean canSplit(Player player) {
        return player.getMoney() >= bet
                && handOpen
                && size() == 2
                && getCards().get(0).pointValue() == getCards().get(1).pointValue();
    }

    public PlayerHand split(Player player, Shoe shoe) {
        if (!canSplit(player)) {
            nextAction = Action.NONE_SELECTED;
            throw new BlackJackRulesException("Splitting is not available.");
        }
        var newHand = new PlayerHand(getMoneyFromPlayer(player, bet));
        newHand.addCard(getCards().get(1));
        getCards().remove(1);
        addCard(getCardFromShoe(shoe));
        newHand.addCard(getCardFromShoe(shoe));
        nextAction = Action.NONE_SELECTED;
        return newHand;
    }

    public boolean hasInsurance() {
        return insurance;
    }

    public boolean canBuyInsurance(Player player, DealerHand dealerHand) {
        var requiredMoney = bet % 2 == 0
                ? bet / 2
                : bet / 2 + 1;
        return player.getMoney() >= requiredMoney
                && handOpen
                && !insurance
                && size() == 2
                && dealerHand.getCards().stream().anyMatch(card -> card.getFace().equals(Card.Face.ACE));
    }

    // Since we are dealing with ints, insurance requires the player to bet up to an even number
    public void buyInsurance(Player player, DealerHand dealerHand) {
        if (!canBuyInsurance(player, dealerHand)) {
            throw new BlackJackRulesException("Hand not eligible for insurance");
        }
        if (bet % 2 != 0) {
            bet = bet + getMoneyFromPlayer(player, 1);
        }
        getMoneyFromPlayer(player, bet / 2);
        insurance = true;
    }

    public enum Action {
        NONE_SELECTED, HIT, STAY, SPLIT, DOUBLE_DOWN, INSURANCE, NOT_AVAILABLE
    }

    private Card getCardFromShoe(Shoe shoe) {
        return shoe.draw().orElseThrow(() -> new NoCardsAvailableException("No cards left in shoe."));
    }

    private int getMoneyFromPlayer(Player player, int amount) {
        if (player.getMoney() < amount) {
            nextAction = Action.NONE_SELECTED;
            throw new BlackJackRulesException("Not enough funds for action.");
        }
        player.setMoney(player.getMoney() - amount);
        return amount;
    }
}
