package ru.bmsgroup.kafkastreamproba.operations;

import ru.bmsgroup.kafkastreamproba.model.TerminalOperationType;

public interface OperationFactory {
    OperationTransformer get(TerminalOperationType type);
}
