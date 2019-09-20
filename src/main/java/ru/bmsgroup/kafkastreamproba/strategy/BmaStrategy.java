package ru.bmsgroup.kafkastreamproba.strategy;

import ru.bmsgroup.kafkastreamproba.model.TerminalOperation;
import ru.bmsgroup.kafkastreamproba.model.TransformedOperation;

import java.util.List;

public interface BmaStrategy {
    List<TransformedOperation.TransformedOperationBuilder> get(TerminalOperation.Identity operation);
}
