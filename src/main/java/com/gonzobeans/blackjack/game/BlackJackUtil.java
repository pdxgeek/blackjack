package com.gonzobeans.blackjack.game;

import com.gonzobeans.blackjack.exception.BlackJackRulesException;
import com.gonzobeans.blackjack.exception.NoCardsAvailableException;
import com.gonzobeans.blackjack.model.Card;
import com.gonzobeans.blackjack.model.Player;
import com.gonzobeans.blackjack.model.Shoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public final class BlackJackUtil {

    private BlackJackUtil() {
        // Utility classes do not have public constructors
    }

   public static Card getCardFromShoe(Shoe shoe) {
        return shoe.draw().orElseThrow(() -> new NoCardsAvailableException("No cards left in shoe."));
    }

    public static int getMoneyFromPlayer(Player player, int amount) {
        if (player.getMoney() < amount) {
            throw new BlackJackRulesException(format("Player %s does not have enough funds. Requested: %s, "
                    + "Available: %s", player.getId(), amount, player.getMoney()));
        }
        player.setMoney(player.getMoney() - amount);
        return amount;
    }

    public static void payoutToPlayer(Player player, int amount) {
        player.setMoney(player.getMoney() + amount);
    }

    public static <K, V> void addToArrayMap(K key, V value, Map<K, List<V>> map) {
        if (map.containsKey(key)) {
            map.get(key).add(value);
        } else {
            map.put(key, new ArrayList<>(List.of(value)));
        }
    }
}
