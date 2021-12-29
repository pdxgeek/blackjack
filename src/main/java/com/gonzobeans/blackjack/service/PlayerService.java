package com.gonzobeans.blackjack.service;

import com.gonzobeans.blackjack.exception.PlayerManagementException;
import com.gonzobeans.blackjack.model.Player;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class PlayerService {
    private static final int STARTING_MONEY = 1000;
    private final Map<String, Player> playerMap;

    public PlayerService() {
        playerMap = new HashMap<>();
    }

    public Player registerPlayer(String name) {
        var player = Player.builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .key(UUID.randomUUID().toString())
                .money(STARTING_MONEY)
                .build();
        playerMap.put(player.getId(), player);
        return player;
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