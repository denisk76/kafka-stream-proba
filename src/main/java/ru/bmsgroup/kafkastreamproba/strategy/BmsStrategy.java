package ru.bmsgroup.kafkastreamproba.strategy;

import ru.bmsgroup.kafkastreamproba.model.terminal.TerminalOperation;
import ru.bmsgroup.kafkastreamproba.model.terminal.TransformedOperation;

import java.util.List;

public interface BmsStrategy {
    List<TransformedOperation.TransformedOperationBuilder> get(TerminalOperation.Identity operation);
}
