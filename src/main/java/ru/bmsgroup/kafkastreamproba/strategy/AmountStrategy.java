package ru.bmsgroup.kafkastreamproba.strategy;

import ru.bmsgroup.kafkastreamproba.model.Money;
import ru.bmsgroup.kafkastreamproba.model.terminal.TransformedOperation;

public interface AmountStrategy {
    Money get(TransformedOperation operation);
}
