package com.gonzobeans.blackjack.service;

import com.gonzobeans.blackjack.exception.BlackJackRulesException;
import com.gonzobeans.blackjack.exception.GameException;
import com.gonzobeans.blackjack.exception.PlayerManagementException;
import com.gonzobeans.blackjack.model.Player;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class PlayerService {
    private static final int STARTING_MONEY = 1000;
    private static PlayerService instance;

    private final Map<String, Player> playerMap;

    private PlayerService() {
        playerMap = new HashMap<>();
        if (instance != null) {
            throw new IllegalStateException();
        }
        instance = this;
    }

    public static synchronized PlayerService getInstance() {
        if (instance == null) {
            instance = new PlayerService();
        }
        return instance;
    }

    public Player registerPlayer(String name) {
        if (StringUtils.isBlank(name)) {
            throw new PlayerManagementException("Player name must not be blank.");
        }
        var player = Player.builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .key(UUID.randomUUID().toString())
                .money(STARTING_MONEY)
                .build();
        playerMap.put(player.getId(), player);
        return player;
    }

    public Optional<Player> getPlayer(String playerId) {
        return Optional.ofNullable(playerMap.get(playerId));
    }

    public double checkFunds(String playerId) {
        var player = getPlayer(playerId)
                .orElseThrow(() -> new GameException("Player " + playerId + "does not exist"));
        return player.getMoney();
    }

    public int withdraw(String playerId, int amount) {
        var player = getPlayer(playerId)
                .orElseThrow(() -> new GameException("Player " + playerId + "does not exist"));
        if (player.getMoney() < amount) {
            throw new BlackJackRulesException(format("Player %s does not have enough funds. Requested: %s, "
                    + "Available: %s", player.getId(), amount, player.getMoney()));
        }
        player.setMoney(player.getMoney() - amount);
        return amount;
    }

    public void payoutToPlayer(String playerId, double amount) {
        var player = getPlayer(playerId)
                .orElseThrow(() -> new GameException("Player " + playerId + "does not exist"));
        player.setMoney(player.getMoney() + amount);
    }

    public List<String> listPlayers() {
        return playerMap.values().stream()
                .map(player -> player.getId() + ": " + player.getName())
                .collect(Collectors.toList());
    }

    public void removePlayer(String playerId) {
        if (!playerMap.containsKey(playerId)) {
            throw new PlayerManagementException(format("Player with ID %s does not exist", playerId));
        }
        playerMap.remove(playerId);
    }
}