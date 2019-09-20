package ru.bmsgroup.kafkastreamproba.strategy;

import ru.bmsgroup.kafkastreamproba.model.TerminalOperationType;
import ru.bmsgroup.kafkastreamproba.strategy.amount.CancelAmountStrategy;
import ru.bmsgroup.kafkastreamproba.strategy.amount.CorrectAmountStrategy;
import ru.bmsgroup.kafkastreamproba.strategy.amount.LockAmountStrategy;
import ru.bmsgroup.kafkastreamproba.strategy.amount.NewAmountStrategy;

public class StandardAmountStrategyFactory implements AmountStrategyFactory {
    @Override
    public AmountStrategy get(TerminalOperationType type) {
        switch (type) {
            case PAYMENT:
                return new NewAmountStrategy();
            case CONFIRM:
                return new LockAmountStrategy();
            case CANCEL:
                return new CancelAmountStrategy();
            case CASH:
                return new CorrectAmountStrategy();
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
