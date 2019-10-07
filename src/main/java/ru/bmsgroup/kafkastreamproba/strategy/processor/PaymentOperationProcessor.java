package ru.bmsgroup.kafkastreamproba.strategy.processor;

import ru.bmsgroup.kafkastreamproba.model.BmsActionType;
import ru.bmsgroup.kafkastreamproba.model.BmsOperationType;
import ru.bmsgroup.kafkastreamproba.model.BmsState;
import ru.bmsgroup.kafkastreamproba.model.terminal.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.strategy.AmountStrategy;
import ru.bmsgroup.kafkastreamproba.strategy.BmsStrategy;
import ru.bmsgroup.kafkastreamproba.strategy.OperationProcessor;
import ru.bmsgroup.kafkastreamproba.strategy.amount.NewAmountStrategy;
import ru.bmsgroup.kafkastreamproba.strategy.id.PurchaseBmsStrategy;

public class PaymentOperationProcessor implements OperationProcessor {
    @Override
    public BmsActionType getAction() {
        return BmsActionType.NEW;
    }

    @Override
    public BmsState getState(TransformedOperation operation) {
        return BmsState.NEW;
    }

    @Override
    public BmsStrategy getBmsStrategy() {
        return new PurchaseBmsStrategy(BmsOperationType.PAYMENT);
    }

    @Override
    public AmountStrategy getAmountStrategy() {
        return new NewAmountStrategy();
    }
}
