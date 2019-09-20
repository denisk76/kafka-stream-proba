package ru.bmsgroup.kafkastreamproba.strategy.amount;

import ru.bmsgroup.kafkastreamproba.model.BmsTransaction;
import ru.bmsgroup.kafkastreamproba.model.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.strategy.AmountStrategy;

import java.math.BigDecimal;

public class CancelAmountStrategy implements AmountStrategy {
    @Override
    public BmsTransaction.Money get(TransformedOperation operation) {
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal locked = BigDecimal.ZERO;
        BigDecimal calculation = BigDecimal.ZERO;
        switch (operation.getBmsOperation().getState()) {
            case NEW:
            case CANCELED:
                break;
            case READY:
                locked = operation.getBmsOperation().getLocked().negate();
                break;
            case PROCESSED:
                amount = operation.getBmsOperation().getAmount().negate();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + operation.getBmsOperation().getState());
        }
        return BmsTransaction.Money.builder()
                .calculation(calculation)
                .amount(amount)
                .locked(locked)
                .build();
    }
}
