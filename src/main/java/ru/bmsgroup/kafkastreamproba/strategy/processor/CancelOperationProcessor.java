package ru.bmsgroup.kafkastreamproba.strategy.processor;

import lombok.extern.java.Log;
import ru.bmsgroup.kafkastreamproba.model.BmsActionType;
import ru.bmsgroup.kafkastreamproba.model.BmsState;
import ru.bmsgroup.kafkastreamproba.model.terminal.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.operations.OperationFinder;
import ru.bmsgroup.kafkastreamproba.operations.PurchaseFinder;
import ru.bmsgroup.kafkastreamproba.strategy.AmountStrategy;
import ru.bmsgroup.kafkastreamproba.strategy.BmsStrategy;
import ru.bmsgroup.kafkastreamproba.strategy.OperationProcessor;
import ru.bmsgroup.kafkastreamproba.strategy.amount.CancelAmountStrategy;
import ru.bmsgroup.kafkastreamproba.strategy.id.ActionBmsStrategy;

@Log
public class CancelOperationProcessor implements OperationProcessor {
    private PurchaseFinder purchaseFinder;
    private OperationFinder operationFinder;

    public CancelOperationProcessor(PurchaseFinder purchaseFinder, OperationFinder operationFinder) {
        this.purchaseFinder = purchaseFinder;
        this.operationFinder = operationFinder;
    }

    @Override
    public BmsActionType getAction() {
        return BmsActionType.CANCEL;
    }

    @Override
    public BmsState getState(TransformedOperation operation) {
        return BmsState.CANCELED;
    }

    @Override
    public BmsStrategy getBmsStrategy() {
        return new ActionBmsStrategy(purchaseFinder, operationFinder);
    }

    @Override
    public AmountStrategy getAmountStrategy() {
        return new CancelAmountStrategy();
    }
}
