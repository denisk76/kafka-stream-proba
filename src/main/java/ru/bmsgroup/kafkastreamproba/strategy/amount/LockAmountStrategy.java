package ru.bmsgroup.kafkastreamproba.strategy.amount;

import ru.bmsgroup.kafkastreamproba.model.BmsTransaction;
import ru.bmsgroup.kafkastreamproba.model.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.strategy.AmountStrategy;

import java.math.BigDecimal;

public class LockAmountStrategy implements AmountStrategy {
    @Override
    public BmsTransaction.Money get(TransformedOperation operation) {
        return BmsTransaction.Money.builder()
                .amount(operation.getBmsOperation().getAmount().negate())
                .locked(operation.getBmsOperation().getAmount())
                .calculation(BigDecimal.ZERO)
                .build();
    }
}
