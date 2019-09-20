package ru.bmsgroup.kafkastreamproba.strategy.id;

import ru.bmsgroup.kafkastreamproba.model.*;
import ru.bmsgroup.kafkastreamproba.operations.OperationFinder;
import ru.bmsgroup.kafkastreamproba.operations.PurchaseFinder;
import ru.bmsgroup.kafkastreamproba.strategy.BmaStrategy;

import java.util.List;
import java.util.stream.Collectors;

public class ActionToPurchaseBmaStrategy implements BmaStrategy {
    private PurchaseFinder purchaseFinder;
    private OperationFinder operationFinder;

    public ActionToPurchaseBmaStrategy(PurchaseFinder purchaseFinder, OperationFinder operationFinder) {
        this.purchaseFinder = purchaseFinder;
        this.operationFinder = operationFinder;
    }

    @Override
    public List<TransformedOperation.TransformedOperationBuilder> get(TerminalOperation.Identity operation) {
        BmsPurchase bmsPurchase = purchaseFinder.get(operation.parentRrn);
        return bmsPurchase.getOperations().stream()
                .map(o -> transformOperation(operation, o))
                .collect(Collectors.toList());
    }

    private TransformedOperation.TransformedOperationBuilder transformOperation(TerminalOperation.Identity terminalOperation, BmsSimpleOperation bmsOperation) {
        BmsOperation operation = operationFinder.get(bmsOperation.getOperationId());
        return TransformedOperation.builder()
                .identity(BmsTransaction.Identity.builder()
                        .purchaseId(terminalOperation.parentRrn)
                        .operationId(bmsOperation.getOperationId())
                        .actionId(terminalOperation.currentRrn)
                        .build())
                .bmsOperation(operation);
    }
}
