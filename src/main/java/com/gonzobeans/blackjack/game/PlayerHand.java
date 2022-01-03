package com.gonzobeans.blackjack.game;

import com.gonzobeans.blackjack.exception.BlackJackRulesException;
import com.gonzobeans.blackjack.dto.HandInformation;
import com.gonzobeans.blackjack.service.PlayerService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

import static com.gonzobeans.blackjack.game.Action.DOUBLE_DOWN;
import static com.gonzobeans.blackjack.game.Action.HIT;
import static com.gonzobeans.blackjack.game.Action.SPLIT;
import static com.gonzobeans.blackjack.game.Action.STAY;

@Slf4j
@Getter
public class PlayerHand extends Hand {
    private static final Set<Action> HAND_ACTIONS = Set.of(HIT, STAY, DOUBLE_DOWN, SPLIT);
    private final String playerId;
    private int bet;
    @Setter
    private HandStatus handStatus;
    private Action nextAction;
    private boolean handOpen;
    private Insurance insurance;

    public PlayerHand(String playerId, int bet) {
        super();
        this.playerId = playerId;
        this.bet = bet;
        this.handOpen = true;
        this.insurance = Insurance.NA;
        handStatus = HandStatus.READY_TO_DEAL;
        nextAction = Action.NONE_SELECTED;
    }

    public void setNextAction(Action action) {
        if (!HAND_ACTIONS.contains(action)) {
            throw new IllegalStateException("Unsupported action passed to hand: " + action.name());
        }
        nextAction = action;
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
        addCard(shoe.draw());
        return isPlayable()
                ? Action.NONE_SELECTED
                : Action.NOT_AVAILABLE;
    }

    public boolean canDoubleDown() {
        return PlayerService.getInstance().checkFunds(playerId) >= bet
                && handOpen
                && size() == 2
                && calculateValue() >= 9
                && calculateValue() <= 11;
    }

    public void doubleDown(Shoe shoe) {
        if (!canDoubleDown()) {
            nextAction = Action.NONE_SELECTED;
            throw new BlackJackRulesException("Hand is not eligible to double down");
        }
        bet = bet + PlayerService.getInstance().withdraw(playerId, bet);
        addCard(shoe.draw());
        this.handOpen = false;
        nextAction = Action.NOT_AVAILABLE;
    }

    public boolean canSplit() {
        return PlayerService.getInstance().checkFunds(playerId) >= bet
                && handOpen
                && size() == 2
                && getCards().get(0).getFace().equals(getCards().get(1).getFace());
    }

    public PlayerHand split(Shoe shoe) {
        if (!canSplit()) {
            nextAction = Action.NONE_SELECTED;
            throw new BlackJackRulesException("Splitting is not available.");
        }
        var newHand = new PlayerHand(playerId, PlayerService.getInstance().withdraw(playerId, bet));
        newHand.addCard(getCards().get(1));
        getCards().remove(1);
        addCard(shoe.draw());
        newHand.addCard(shoe.draw());
        nextAction = Action.NONE_SELECTED;
        return newHand;
    }

    public void offerInsurance() {
        insurance = Insurance.WAITING;
    }

    public boolean insuranceSelected() {
        return insurance.equals(Insurance.DECLINED) || insurance.equals(Insurance.ACCEPTED);
    }

    public boolean canBuyInsurance() {
        var requiredMoney = bet / 2;
        return PlayerService.getInstance().checkFunds(playerId) >= requiredMoney
                && handOpen
                && !insurance.equals(Insurance.NA)
                && !insurance.equals(Insurance.ACCEPTED)
                && size() == 2;
    }

    public void buyInsurance() {
        if (!canBuyInsurance()) {
            throw new BlackJackRulesException("Hand not eligible for insurance");
        }
        PlayerService.getInstance().withdraw(playerId, bet / 2);
        insurance = Insurance.ACCEPTED;
    }

    public void declineInsurance() {
        insurance = Insurance.DECLINED;
    }

    @Override
    public HandInformation getInfo() {
        return HandInformation.builder()
                .id(getId())
                .status(handStatus)
                .insurance(insurance)
                .bet(bet)
                .cards(getCards().stream().map(Card::toCode).toList())
                .handValue(calculateValue() > 0 ? calculateValue() : null)
                .build();
    }

    public enum Insurance {
        NA, WAITING, ACCEPTED, DECLINED
    }

    public enum HandStatus {
        READY_TO_DEAL, WAITING_FOR_INSURANCE, INSURANCE_SELECTED, WAITING_FOR_TURN, CURRENT_TURN, WAITING_TO_RESOLVE,
        WIN, LOSS, PUSH, INSURANCE_PAID
    }


}
