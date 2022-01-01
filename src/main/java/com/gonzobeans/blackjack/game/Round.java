package com.gonzobeans.blackjack.game;

import com.gonzobeans.blackjack.exception.BlackJackRulesException;
import com.gonzobeans.blackjack.model.Player;
import com.gonzobeans.blackjack.model.Shoe;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;


import java.util.List;

import static com.gonzobeans.blackjack.game.BlackJackUtil.getCardFromShoe;
import static com.gonzobeans.blackjack.game.BlackJackUtil.getMoneyFromPlayer;

public class Round {
    private List<Player> players;
    private Shoe shoe;
    private DealerHand dealerHand;
    private MultiValuedMap<Player, PlayerHand> playerHands;
    private int minimumBet;

    public Round(List<Player> players, Shoe shoe, int minimumBet) {
        this.players = players;
        this.playerHands = new ArrayListValuedHashMap<>();
        this.minimumBet = minimumBet;
        this.dealerHand = new DealerHand();
    }

    public PlayerHand takeBet(Player player, int bet) {
        if (bet < minimumBet) {
            throw new BlackJackRulesException("Must place a minimum bet of " + minimumBet);
        }
        var playerHand = new PlayerHand(getMoneyFromPlayer(player, bet));
        playerHands.put(player, playerHand);
        return playerHand;
    }

    public void dealCards() {
        // First Card
        playerHands.values().forEach(hand -> hand.addCard(getCardFromShoe(shoe)));
        dealerHand.addCard(getCardFromShoe(shoe));
        //Second Card
        playerHands.values().forEach(hand -> hand.addCard(getCardFromShoe(shoe)));
        dealerHand.addCard(getCardFromShoe(shoe));
    }
}
