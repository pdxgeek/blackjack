package com.gonzobeans.blackjack.util;

import com.gonzobeans.blackjack.game.DealerHand;
import com.gonzobeans.blackjack.game.Hand;
import com.gonzobeans.blackjack.game.PlayerHand;
import com.gonzobeans.blackjack.service.PlayerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.gonzobeans.blackjack.game.PlayerHand.Insurance.ACCEPTED;

public final class GameUtil {

    private GameUtil() {
        // Utility classes do not have public constructors
    }

    public static void resolveGame(DealerHand dealerHand, List<PlayerHand> playerHands) {
        if (dealerHand.blackjack()) {
            playerHands.forEach(GameUtil::resolveDealerBlackJack);

        } else {
            playerHands.stream()
                    .filter(Hand::notBusted)
                    .forEach(hand -> resolveDealerNonBlackJack(dealerHand, hand));
        }
    }

    public static <K, V> void addToArrayMap(K key, V value, Map<K, List<V>> map) {
        if (map.containsKey(key)) {
            map.get(key).add(value);
        } else {
            map.put(key, new ArrayList<>(List.of(value)));
        }
    }

    private static void resolveDealerNonBlackJack(DealerHand dealerHand, PlayerHand hand) {
        if (hand.blackjack()) {
            PlayerService.getInstance().payoutToPlayer(hand.getPlayerId(), (hand.getBet() * 1.5));
        } else if (hand.calculateValue() > dealerHand.calculateValue()) {
            PlayerService.getInstance().payoutToPlayer(hand.getPlayerId(), hand.getBet());
        }
    }

    private static void resolveDealerBlackJack(PlayerHand hand) {
        if (hand.getInsurance().equals(ACCEPTED)) {
            if (hand.blackjack()) {
                PlayerService.getInstance().payoutToPlayer(hand.getPlayerId(), (double) hand.getBet() / 2);
            }
            PlayerService.getInstance().payoutToPlayer(hand.getPlayerId(), hand.getBet());
        }
    }
}
