package com.gonzobeans.blackjack.controller;

import com.gonzobeans.blackjack.model.Player;
import com.gonzobeans.blackjack.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService playerService;

    @PostMapping
    public Player registerPlayer(@RequestParam String name) {
        return playerService.registerPlayer(name);
    }

    @GetMapping
    public List<String> listPlayers() {
        return playerService.listPlayers();
    }

    @DeleteMapping
    public void removePlayer(@RequestParam String playerId) {
        playerService.removePlayer(playerId);
    }
}
