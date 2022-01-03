package com.gonzobeans.blackjack.game;

import com.gonzobeans.blackjack.exception.BlackJackTableException;
import com.gonzobeans.blackjack.model.Player;
import com.gonzobeans.blackjack.model.TableInformation;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Getter
public class Table {
    private static final int MAX_PLAYERS = 7;
    private final String id;
    private final String name;
    private final int minimumBet;
    private final Map<Integer, Player> seats;

    private Round currentRound;

    public Table(String name, int minimumBet) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.minimumBet = minimumBet;
        seats = new HashMap<>();
    }

    public TableInformation getInformation() {
        return TableInformation.builder()
                .id(getId())
                .name(getName())
                .emptySeats(MAX_PLAYERS - getSeatedPlayers().size())
                .maximumSeats(MAX_PLAYERS)
                .build();
    }

    public Optional<Player> getPlayerBySeat(int seatNumber) {
        validateSeatNumber(seatNumber);
        return Optional.ofNullable(seats.get(seatNumber));
    }

    public void seatPlayer(int seatNumber, Player player) {
        validateSeatNumber(seatNumber);
        getPlayerBySeat(seatNumber).ifPresentOrElse(p -> {
                throw new BlackJackTableException("Player " + p.getId() + " is already seated here.");
            }, () -> seats.put(seatNumber, player));
    }

    private List<Player> getSeatedPlayers() {
        var playerList = new ArrayList<Player>();
        for (int i = 1; i <= 7; i++) {
            getPlayerBySeat(i).ifPresent(playerList::add);
        }
        return playerList;
    }

    private void validateSeatNumber(int seatNumber) {
        if (seatNumber < 1 || seatNumber > MAX_PLAYERS) {
            throw new BlackJackTableException("Invalid Seat Number: " + seatNumber);
        }
    }
}
