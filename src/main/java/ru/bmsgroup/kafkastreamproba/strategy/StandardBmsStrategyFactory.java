package ru.bmsgroup.kafkastreamproba.strategy;

import ru.bmsgroup.kafkastreamproba.model.TerminalOperationType;
import ru.bmsgroup.kafkastreamproba.operations.OperationFinder;
import ru.bmsgroup.kafkastreamproba.operations.PurchaseFinder;
import ru.bmsgroup.kafkastreamproba.strategy.id.ActionBmaStrategy;
import ru.bmsgroup.kafkastreamproba.strategy.id.ActionToOperationBmaStrategy;
import ru.bmsgroup.kafkastreamproba.strategy.id.OperationBmaStrategy;
import ru.bmsgroup.kafkastreamproba.strategy.id.PurchaseBmaStrategy;

public class StandardBmsStrategyFactory implements BmsStrategyFactory {
    private PurchaseFinder purchaseFinder;
    private OperationFinder operationFinder;

    public StandardBmsStrategyFactory(PurchaseFinder purchaseFinder, OperationFinder operationFinder) {
        this.purchaseFinder = purchaseFinder;
        this.operationFinder = operationFinder;
    }

    @Override
    public BmaStrategy get(TerminalOperationType type) {
        switch (type) {
            case PAYMENT:
                return new PurchaseBmaStrategy();
            case CONFIRM:
                return new ActionToOperationBmaStrategy(operationFinder);
            case CANCEL:
                return new ActionBmaStrategy(purchaseFinder, operationFinder);
            case CASH:
                return new OperationBmaStrategy(purchaseFinder);
            default:
                return null;
        }
    }
}
