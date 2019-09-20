package ru.bmsgroup.kafkastreamproba.strategy.id;

import ru.bmsgroup.kafkastreamproba.model.BmsOperation;
import ru.bmsgroup.kafkastreamproba.model.TerminalOperation;
import ru.bmsgroup.kafkastreamproba.model.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.operations.OperationFinder;
import ru.bmsgroup.kafkastreamproba.operations.PurchaseFinder;
import ru.bmsgroup.kafkastreamproba.strategy.BmaStrategy;

import java.util.List;

public class ActionBmaStrategy implements BmaStrategy {
    private PurchaseFinder purchaseFinder;
    private OperationFinder operationFinder;

    public ActionBmaStrategy(PurchaseFinder purchaseFinder, OperationFinder operationFinder) {
        this.purchaseFinder = purchaseFinder;
        this.operationFinder = operationFinder;
    }

    @Override
    public List<TransformedOperation.TransformedOperationBuilder> get(TerminalOperation.Identity operation) {
        BmsOperation bmsOperation = operationFinder.get(operation.parentRrn);
        if (bmsOperation != null) {
            ActionToOperationBmaStrategy strategy = new ActionToOperationBmaStrategy(operationFinder);
            return strategy.get(operation);
        } else {
            ActionToPurchaseBmaStrategy strategy = new ActionToPurchaseBmaStrategy(purchaseFinder, operationFinder);
            return strategy.get(operation);
        }
    }
}
