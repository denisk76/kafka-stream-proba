package ru.bmsgroup.kafkastreamproba.strategy;

import ru.bmsgroup.kafkastreamproba.model.TerminalOperationType;

public interface BmsStrategyFactory {
    BmaStrategy get(TerminalOperationType type);
}
