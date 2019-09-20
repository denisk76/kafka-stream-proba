package ru.bmsgroup.kafkastreamproba.strategy.amount;

import ru.bmsgroup.kafkastreamproba.model.BmsTransaction;
import ru.bmsgroup.kafkastreamproba.model.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.strategy.AmountStrategy;

import java.math.BigDecimal;

public class NewAmountStrategy implements AmountStrategy {
    @Override
    public BmsTransaction.Money get(TransformedOperation operation) {
        return BmsTransaction.Money.builder()
                .calculation(operation.getData().amount)
                .amount(BigDecimal.ZERO)
                .locked(BigDecimal.ZERO)
                .build();
    }
}
