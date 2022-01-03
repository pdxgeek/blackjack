package com.gonzobeans.blackjack.game;

import com.gonzobeans.blackjack.exception.BlackJackRulesException;
import com.gonzobeans.blackjack.exception.BlackJackTableException;
import com.gonzobeans.blackjack.model.Player;
import com.gonzobeans.blackjack.model.Shoe;
import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static com.fasterxml.jackson.core.JsonToken.NOT_AVAILABLE;
import static com.gonzobeans.blackjack.game.BlackJackUtil.addToArrayMap;
import static com.gonzobeans.blackjack.game.BlackJackUtil.getCardFromShoe;
import static com.gonzobeans.blackjack.game.BlackJackUtil.getMoneyFromPlayer;
import static com.gonzobeans.blackjack.game.BlackJackUtil.payoutToPlayer;
import static com.gonzobeans.blackjack.model.Action.DOUBLE_DOWN;
import static com.gonzobeans.blackjack.model.Action.HIT;
import static com.gonzobeans.blackjack.model.Action.SPLIT;
import static com.gonzobeans.blackjack.game.PlayerHand.HandStatus.CURRENT_TURN;
import static com.gonzobeans.blackjack.game.PlayerHand.HandStatus.INSURANCE_SELECTED;
import static com.gonzobeans.blackjack.game.PlayerHand.HandStatus.WAITING_FOR_INSURANCE;
import static com.gonzobeans.blackjack.game.PlayerHand.HandStatus.WAITING_FOR_TURN;
import static com.gonzobeans.blackjack.game.PlayerHand.HandStatus.WAITING_TO_RESOLVE;
import static com.gonzobeans.blackjack.game.PlayerHand.Insurance.ACCEPTED;
import static com.gonzobeans.blackjack.model.Action.STAY;
import static com.gonzobeans.blackjack.model.Card.Face.ACE;

public class Round {
    @Getter
    private final String id;
    private final List<Player> players;
    private final Shoe shoe;
    private final DealerHand dealerHand;
    private final Map<Player, List<PlayerHand>> playerHands;
    private final int minimumBet;
    @Getter
    private RoundStatus roundStatus;

    public Round(List<Player> players, Shoe shoe, int minimumBet) {
        this.id = UUID.randomUUID().toString();
        this.players = players;
        this.shoe = shoe;
        this.playerHands = new HashMap<>();
        this.minimumBet = minimumBet;
        this.dealerHand = new DealerHand();
        this.roundStatus = RoundStatus.NOT_STARTED;
    }

    public void start() {
        if (players.isEmpty()) {
            throw new BlackJackTableException("Must have at least one player to start game");
        }
        roundStatus = RoundStatus.BETS;
    }

    public void runGame() {
        if (playerHands.size() == players.size()) {
            dealCards();
            roundStatus = RoundStatus.TURNS;
            checkInsurance();
            processTurns();
        }
    }

    public void takeBet(Player player, int bet) {
        if (playerHands.containsKey(player)) {
            throw new BlackJackRulesException("Player has already bet.");
        }
        if (roundStatus != RoundStatus.BETS) {
            throw new BlackJackRulesException("Cannot place a bet at this time.");
        }
        if (bet < minimumBet) {
            throw new BlackJackRulesException("Must place a minimum bet of " + minimumBet);
        }
        var playerHand = new PlayerHand(player, getMoneyFromPlayer(player, bet));
        addToArrayMap(player, playerHand, playerHands);
        runGame();
    }

    public void dealCards() {
        // First Card
        getAllPlayerHands().forEach(hand -> hand.addCard(getCardFromShoe(shoe)));
        dealerHand.addCard(getCardFromShoe(shoe));
        //Second Card
        getAllPlayerHands().forEach(hand -> {
            hand.addCard(getCardFromShoe(shoe));
            hand.setHandStatus(WAITING_FOR_TURN);
        });
        dealerHand.addCard(getCardFromShoe(shoe));
    }

    public void buyInsurance(PlayerHand hand) {
        hand.buyInsurance();
        hand.setHandStatus(INSURANCE_SELECTED);
        // TODO: catch not enough funds and set to auto-decline
    }

    public void declineInsurance(PlayerHand hand) {
        if (!hand.getHandStatus().equals(WAITING_FOR_INSURANCE)) {
            throw new BlackJackRulesException("Insurance selected or not available at this time.");
        }
        hand.declineInsurance();
        hand.setHandStatus(INSURANCE_SELECTED);
    }

    private void checkInsurance() {
        if (dealerHand.getCards().get(1).getFace().equals(ACE)) {
            players.stream()
                    .flatMap(player -> playerHands.get(player).stream())
                    .forEach(PlayerHand::offerInsurance);
            processInsurance();
        }
    }

    private void processInsurance() {
        var continueProcessing = false;
        for (var player : players) {
            for (var hand : playerHands.get(player)) {
                if (hand.getHandStatus().equals(WAITING_FOR_INSURANCE)) {
                    continueProcessing = true;
                    break;
                }
            }
        }
        // TODO: Send Status
        if (continueProcessing) {
            processInsurance();
        }
    }

    private void processTurns() {
        var continueProcessing = true;
        for (var player : players) {
            for (var hand : playerHands.get(player)) {
                if (hand.getHandStatus().equals(WAITING_FOR_TURN) && continueProcessing) {
                    continueProcessing = false;
                    hand.setHandStatus(CURRENT_TURN);
                } else if (hand.getHandStatus().equals(CURRENT_TURN)) {
                    continueProcessing = false;
                    processTurn(hand);
                }
            }
        }
        if (continueProcessing) {
            resolve();
        }
    }

    private void resolve() {
        roundStatus = RoundStatus.RESOLUTION;
        showDealerCards();
        if (dealerHand.blackjack()) {
            getAllPlayerHands().forEach(this::resolveDealerBlackJack);

        } else {
            getAllPlayerHands()
                    .filter(Hand::notBusted)
                    .forEach(this::resolveDealerNonBlackJack);
        }
        roundStatus = RoundStatus.COMPLETE;
    }

    private void resolveDealerNonBlackJack(PlayerHand hand) {
        if (hand.blackjack()) {
            payoutToPlayer(hand.getPlayer(), (int) (hand.getBet() * 1.5));
        } else if (hand.calculateValue() > dealerHand.calculateValue()) {
            payoutToPlayer(hand.getPlayer(), hand.getBet());
        }
    }

    private void resolveDealerBlackJack(PlayerHand hand) {
        if (hand.getInsurance().equals(ACCEPTED)) {
            if (hand.blackjack()) {
                payoutToPlayer(hand.getPlayer(), hand.getBet() / 2);
            }
            payoutToPlayer(hand.getPlayer(), hand.getBet());
        }
    }

    private void showDealerCards() {
        dealerHand.revealCards();
        // TODO: Add logic to push reveal message
    }

    private void processTurn(PlayerHand hand) {
        switch (hand.getNextAction()) {
            case STAY:
                hand.stay();
                hand.setHandStatus(WAITING_TO_RESOLVE);
                break;
            case HIT:
                if (hand.hit(shoe).equals(NOT_AVAILABLE)) {
                    hand.setHandStatus(WAITING_TO_RESOLVE);
                }
                break;
            case DOUBLE_DOWN:
                hand.doubleDown(shoe);
                hand.setHandStatus(WAITING_TO_RESOLVE);
                break;
            case SPLIT:
                var newHand = hand.split(shoe);
                addToArrayMap(hand.getPlayer(), newHand, playerHands);
                break;
            default:
                break;
        }
        processTurns();
    }

    private Stream<PlayerHand> getAllPlayerHands() {
        return playerHands.values().stream().flatMap(Collection::stream);
    }

    private enum RoundStatus {
        NOT_STARTED, BETS, TURNS, RESOLUTION, COMPLETE
    }
}
