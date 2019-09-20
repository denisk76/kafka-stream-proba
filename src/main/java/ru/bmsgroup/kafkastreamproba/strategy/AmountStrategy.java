package ru.bmsgroup.kafkastreamproba.strategy;

import ru.bmsgroup.kafkastreamproba.model.BmsTransaction;
import ru.bmsgroup.kafkastreamproba.model.TransformedOperation;

public interface AmountStrategy {
    BmsTransaction.Money get(TransformedOperation operation);
}
