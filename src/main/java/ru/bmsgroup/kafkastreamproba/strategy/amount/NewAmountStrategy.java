package ru.bmsgroup.kafkastreamproba.strategy.amount;

import ru.bmsgroup.kafkastreamproba.model.Money;
import ru.bmsgroup.kafkastreamproba.model.terminal.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.strategy.AmountStrategy;

import java.math.BigDecimal;

public class NewAmountStrategy implements AmountStrategy {
    @Override
    public Money get(TransformedOperation operation) {
        return Money.builder()
                .calculation(operation.getData().amount)
                .amount(BigDecimal.ZERO)
                .locked(BigDecimal.ZERO)
                .build();
    }
}
