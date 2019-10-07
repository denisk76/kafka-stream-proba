package ru.bmsgroup.kafkastreamproba.strategy.amount;

import lombok.extern.java.Log;
import org.springframework.util.Assert;
import ru.bmsgroup.kafkastreamproba.model.Money;
import ru.bmsgroup.kafkastreamproba.model.terminal.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.strategy.AmountStrategy;

import java.math.BigDecimal;

@Log
public class CancelAmountStrategy implements AmountStrategy {
    @Override
    public Money get(TransformedOperation operation) {
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal locked = BigDecimal.ZERO;
        BigDecimal calculation = BigDecimal.ZERO;
        Assert.notNull(operation.getBmsOperation(), "operation.getBmsOperation() can't be null");
        log.info("operation state: " + operation.getBmsOperation().getState());
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
        return Money.builder()
                .calculation(calculation)
                .amount(amount)
                .locked(locked)
                .build();
    }
}
