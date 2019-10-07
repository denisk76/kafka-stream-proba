package ru.bmsgroup.kafkastreamproba.strategy.processor;

import ru.bmsgroup.kafkastreamproba.model.BmsActionType;
import ru.bmsgroup.kafkastreamproba.model.BmsOperationType;
import ru.bmsgroup.kafkastreamproba.model.BmsState;
import ru.bmsgroup.kafkastreamproba.model.terminal.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.operations.PurchaseFinder;
import ru.bmsgroup.kafkastreamproba.strategy.AmountStrategy;
import ru.bmsgroup.kafkastreamproba.strategy.BmsStrategy;
import ru.bmsgroup.kafkastreamproba.strategy.OperationProcessor;
import ru.bmsgroup.kafkastreamproba.strategy.amount.CorrectAmountStrategy;
import ru.bmsgroup.kafkastreamproba.strategy.id.OperationBmsStrategy;

public class CashOperationProcessor implements OperationProcessor {
    private PurchaseFinder purchaseFinder;

    public CashOperationProcessor(PurchaseFinder purchaseFinder) {
        this.purchaseFinder = purchaseFinder;
    }

    @Override
    public BmsActionType getAction() {
        return BmsActionType.NEW;
    }

    @Override
    public BmsState getState(TransformedOperation operation) {
        return operation.getBmsPurchase().getState();
    }

    @Override
    public BmsStrategy getBmsStrategy() {
        return new OperationBmsStrategy(purchaseFinder, BmsOperationType.CASH);
    }

    @Override
    public AmountStrategy getAmountStrategy() {
        return new CorrectAmountStrategy();
    }
}
