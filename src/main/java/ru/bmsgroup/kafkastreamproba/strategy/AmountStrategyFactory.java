package ru.bmsgroup.kafkastreamproba.strategy;

import ru.bmsgroup.kafkastreamproba.model.TerminalOperationType;

public interface AmountStrategyFactory {
    AmountStrategy get(TerminalOperationType type);
}
