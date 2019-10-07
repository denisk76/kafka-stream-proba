package ru.bmsgroup.kafkastreamproba.strategy.amount;

import org.springframework.util.Assert;
import ru.bmsgroup.kafkastreamproba.model.Money;
import ru.bmsgroup.kafkastreamproba.model.terminal.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.strategy.AmountStrategy;

import java.math.BigDecimal;

public class CorrectAmountStrategy implements AmountStrategy {
    @Override
    public Money get(TransformedOperation operation) {
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal locked = BigDecimal.ZERO;
        BigDecimal calculation = BigDecimal.ZERO;
        Assert.notNull(operation, "operation can't be null");
        Assert.notNull(operation.getBmsPurchase(), "operation.getBmsOperation() can't be null");
        Assert.notNull(operation.getBmsPurchase().getState(), "operation.getBmsOperation().getState() can't be null");
        switch (operation.getBmsPurchase().getState()) {
            case NEW:
                calculation = operation.getData().amount.negate();
                break;
            case CANCELED:
                break;
            case READY:
                locked = operation.getData().amount.negate();
                break;
            case PROCESSED:
                amount = operation.getData().amount.negate();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + operation.getBmsOperation().getState());
        }
        return Money.builder()
                .calculation(calculation)
                .amount(amount)
                .locked(locked)
                .build();
    }
}
