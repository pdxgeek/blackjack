package com.gonzobeans.blackjack.service;

import com.gonzobeans.blackjack.game.Table;
import com.gonzobeans.blackjack.model.TableInformation;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TableService {
    private Map<String, Table> tables;

    public TableService() {
        this.tables = new HashMap<>();
    }

    public void createTable(String name, int minimumBet) {
        // TODO: default name scheme
        // TODO: validate minimum bet
        var newTable = new Table(name, minimumBet);
        tables.put(newTable.getId(), newTable);
    }

    public List<TableInformation> getTableList() {
        return tables.values().stream()
                .map(Table::getInformation)
                .collect(Collectors.toList());
    }
}
