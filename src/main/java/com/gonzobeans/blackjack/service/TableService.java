package com.gonzobeans.blackjack.service;

import com.gonzobeans.blackjack.game.Table;
import com.gonzobeans.blackjack.dto.TableInformation;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final Map<String, Table> tables;

    public TableService() {
        this.tables = new HashMap<>();
    }

    public String createTable(String name, int minimumBet) {
        // TODO: default name scheme
        // TODO: validate minimum bet
        var newTable = new Table(name, minimumBet);
        tables.put(newTable.getId(), newTable);
        return newTable.getId();
    }

    public List<TableInformation> getTableList() {
        return tables.values().stream()
                .map(Table::getInformation)
                .collect(Collectors.toList());
    }

    public Optional<Table> getTable(String tableId) {
        return Optional.ofNullable(tables.get(tableId));
    }
}
