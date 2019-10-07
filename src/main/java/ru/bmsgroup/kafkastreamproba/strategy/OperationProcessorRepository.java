package ru.bmsgroup.kafkastreamproba.strategy;

import ru.bmsgroup.kafkastreamproba.model.terminal.TerminalOperationType;

public interface OperationProcessorRepository {
    OperationProcessor get(TerminalOperationType type);
}
