package ru.bmsgroup.kafkastreamproba.strategy;

import ru.bmsgroup.kafkastreamproba.model.BmsActionType;
import ru.bmsgroup.kafkastreamproba.model.BmsState;
import ru.bmsgroup.kafkastreamproba.model.terminal.TransformedOperation;

public interface OperationProcessor {
    BmsActionType getAction();

    BmsState getState(TransformedOperation operation);

    BmsStrategy getBmsStrategy();

    AmountStrategy getAmountStrategy();
}
