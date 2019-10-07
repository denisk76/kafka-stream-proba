package ru.bmsgroup.kafkastreamproba.strategy.amount;

import ru.bmsgroup.kafkastreamproba.model.Money;
import ru.bmsgroup.kafkastreamproba.model.terminal.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.strategy.AmountStrategy;

import java.math.BigDecimal;

public class LockAmountStrategy implements AmountStrategy {
    @Override
    public Money get(TransformedOperation operation) {
        return Money.builder()
                .amount(BigDecimal.ZERO)
                .locked(operation.getBmsOperation().getCalculation())
                .calculation(operation.getBmsOperation().getCalculation().negate())
                .build();
    }
}
