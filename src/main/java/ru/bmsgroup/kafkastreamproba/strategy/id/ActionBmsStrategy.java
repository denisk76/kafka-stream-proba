package ru.bmsgroup.kafkastreamproba.strategy.id;

import lombok.extern.java.Log;
import ru.bmsgroup.kafkastreamproba.model.BmsOperation;
import ru.bmsgroup.kafkastreamproba.model.BmsPurchase;
import ru.bmsgroup.kafkastreamproba.model.terminal.TerminalOperation;
import ru.bmsgroup.kafkastreamproba.model.terminal.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.operations.OperationFinder;
import ru.bmsgroup.kafkastreamproba.operations.PurchaseFinder;
import ru.bmsgroup.kafkastreamproba.strategy.BmsStrategy;

import java.util.List;

@Log
public class ActionBmsStrategy implements BmsStrategy {
    private PurchaseFinder purchaseFinder;
    private OperationFinder operationFinder;

    public ActionBmsStrategy(PurchaseFinder purchaseFinder, OperationFinder operationFinder) {
        this.purchaseFinder = purchaseFinder;
        this.operationFinder = operationFinder;
    }

    @Override
    public List<TransformedOperation.TransformedOperationBuilder> get(TerminalOperation.Identity operation) {
        BmsOperation bmsOperation = operationFinder.get(operation.parentRrn);
        BmsPurchase bmsPurchase = purchaseFinder.get(operation.parentRrn);
        if (bmsPurchase == null && bmsOperation != null) {
            log.info("this is action to operation");
            ActionToOperationBmsStrategy strategy = new ActionToOperationBmsStrategy(operationFinder);
            return strategy.get(operation);
        } else if (bmsPurchase != null) {
            log.info("this is action to purchase");
            ActionToPurchaseBmsStrategy strategy = new ActionToPurchaseBmsStrategy(purchaseFinder, operationFinder);
            return strategy.get(operation);
        } else throw new StrategyException();
    }
}
