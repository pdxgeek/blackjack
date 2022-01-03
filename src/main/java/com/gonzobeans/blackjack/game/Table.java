package com.gonzobeans.blackjack.game;

import com.gonzobeans.blackjack.dto.PlayerAction;
import com.gonzobeans.blackjack.exception.BlackJackTableException;
import com.gonzobeans.blackjack.dto.TableInformation;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Getter
public class Table {
    private static final int MAX_PLAYERS = 7;
    private static final int DECK_COUNT = 7;
    private final String id;
    private final String name;
    private final int minimumBet;
    private final Map<Integer, String> seats;
    private final Shoe shoe;

    private Round currentRound;

    public Table(String name, int minimumBet) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.minimumBet = minimumBet;
        this.shoe = Shoe.ofStandardCards(DECK_COUNT);
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

    public void processPlayerAction(PlayerAction action) {
        switch (action.getAction()) {
            case SIT_AT_TABLE -> seatPlayer(action.getActionValue(), action.getPlayerId());
            case LEAVE_TABLE -> {
                seats.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(action.getPlayerId()))
                        .findFirst().ifPresent(entry -> seats.put(entry.getKey(), null));
                currentRound.processAction(action);
            }
            default -> {
                if (!StringUtils.equals(action.getRoundId(), currentRound.getId())) {
                    throw new BlackJackTableException("Action submitted for incorrect round.");
                }
                currentRound.processAction(action);
            }
        }
    }

    public Optional<String> getPlayerBySeat(int seatNumber) {
        validateSeatNumber(seatNumber);
        return Optional.ofNullable(seats.get(seatNumber));
    }

    public void seatPlayer(int seatNumber, String playerId) {
        validateSeatNumber(seatNumber);
        getPlayerBySeat(seatNumber).ifPresentOrElse(player -> {
                throw new BlackJackTableException("Player " + player + " is already seated here.");
            }, () -> seats.put(seatNumber, playerId));
    }

    private List<String> getSeatedPlayers() {
        var playerList = new ArrayList<String>();
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
