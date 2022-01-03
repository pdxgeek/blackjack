package com.gonzobeans.blackjack.game;

import com.gonzobeans.blackjack.exception.BlackJackRulesException;
import com.gonzobeans.blackjack.exception.NoCardsAvailableException;
import com.gonzobeans.blackjack.model.Card;
import com.gonzobeans.blackjack.model.Player;
import com.gonzobeans.blackjack.model.Shoe;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PlayerHand extends Hand {
    private final Player player;
    private int bet;
    @Setter

    private HandStatus handStatus;
    private Action nextAction;
    private boolean handOpen;
    private Insurance insurance;
    private int payout;

    public PlayerHand(Player player, int bet) {
        super();
        this.player = player;
        this.bet = bet;
        this.handOpen = true;
        this.insurance = Insurance.NA;
        handStatus = HandStatus.READY_TO_DEAL;
        nextAction = Action.NONE_SELECTED;
    }

    @Override
    public boolean isPlayable() {
        return super.isPlayable() && handOpen;
    }

    public void stay() {
        this.handOpen = false;
        nextAction = Action.NOT_AVAILABLE;

    }

    public Action hit(Shoe shoe) {
        if (!isPlayable()) {
            throw new BlackJackRulesException("Hand is not playable.");
        }
        addCard(getCardFromShoe(shoe));
        return isPlayable()
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

    public void doubleDown(Shoe shoe) {
        if (!canDoubleDown(player)) {
            nextAction = Action.NONE_SELECTED;
            throw new BlackJackRulesException("Hand is not eligible to double down");
        }
        bet = bet + getMoneyFromPlayer(player, bet);
        addCard(getCardFromShoe(shoe));
        this.handOpen = false;
        nextAction = Action.NOT_AVAILABLE;
    }

    public boolean canSplit() {
        return player.getMoney() >= bet
                && handOpen
                && size() == 2
                && getCards().get(0).getFace().equals(getCards().get(1).getFace());
    }

    public PlayerHand split(Shoe shoe) {
        if (!canSplit()) {
            nextAction = Action.NONE_SELECTED;
            throw new BlackJackRulesException("Splitting is not available.");
        }
        var newHand = new PlayerHand(player, getMoneyFromPlayer(player, bet));
        newHand.addCard(getCards().get(1));
        getCards().remove(1);
        addCard(getCardFromShoe(shoe));
        newHand.addCard(getCardFromShoe(shoe));
        nextAction = Action.NONE_SELECTED;
        return newHand;
    }

    public void offerInsurance() {
        insurance = Insurance.WAITING;
    }

    public boolean insuranceSelected() {
        return insurance.equals(Insurance.DECLINED) || insurance.equals(Insurance.ACCEPTED);
    }

    public boolean canBuyInsurance(Player player) {
        var requiredMoney = bet / 2;
        return player.getMoney() >= requiredMoney
                && handOpen
                && !insurance.equals(Insurance.NA)
                && !insurance.equals(Insurance.ACCEPTED)
                && size() == 2;
    }

    public void buyInsurance() {
        if (!canBuyInsurance(player)) {
            throw new BlackJackRulesException("Hand not eligible for insurance");
        }
        getMoneyFromPlayer(player, bet / 2);
        insurance = Insurance.ACCEPTED;
    }

    public void declineInsurance() {
        insurance = Insurance.DECLINED;
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

    public enum Insurance {
        NA, WAITING, ACCEPTED, DECLINED
    }

    public enum Action {
        NONE_SELECTED, STAY, HIT, SPLIT, DOUBLE_DOWN, NOT_AVAILABLE
    }

    public enum HandStatus {
        READY_TO_DEAL, WAITING_FOR_INSURANCE, INSURANCE_SELECTED, WAITING_FOR_TURN, CURRENT_TURN, WAITING_TO_RESOLVE,
        WIN, LOSS, PUSH, INSURANCE_PAID
    }


}
