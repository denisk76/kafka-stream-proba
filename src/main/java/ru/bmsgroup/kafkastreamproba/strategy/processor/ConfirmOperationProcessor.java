package ru.bmsgroup.kafkastreamproba.strategy.processor;

import ru.bmsgroup.kafkastreamproba.model.BmsActionType;
import ru.bmsgroup.kafkastreamproba.model.BmsState;
import ru.bmsgroup.kafkastreamproba.model.terminal.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.operations.OperationFinder;
import ru.bmsgroup.kafkastreamproba.strategy.AmountStrategy;
import ru.bmsgroup.kafkastreamproba.strategy.BmsStrategy;
import ru.bmsgroup.kafkastreamproba.strategy.OperationProcessor;
import ru.bmsgroup.kafkastreamproba.strategy.amount.LockAmountStrategy;
import ru.bmsgroup.kafkastreamproba.strategy.id.ActionToOperationBmsStrategy;

public class ConfirmOperationProcessor implements OperationProcessor {
    private OperationFinder operationFinder;

    public ConfirmOperationProcessor(OperationFinder operationFinder) {
        this.operationFinder = operationFinder;
    }

    @Override
    public BmsActionType getAction() {
        return BmsActionType.CONFIRM;
    }

    @Override
    public BmsState getState(TransformedOperation operation) {
        return BmsState.READY;
    }

    @Override
    public BmsStrategy getBmsStrategy() {
        return new ActionToOperationBmsStrategy(operationFinder);
    }

    @Override
    public AmountStrategy getAmountStrategy() {
        return new LockAmountStrategy();
    }
}
