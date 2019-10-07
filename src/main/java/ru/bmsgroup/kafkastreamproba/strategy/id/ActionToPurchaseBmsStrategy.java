package ru.bmsgroup.kafkastreamproba.strategy.id;

import ru.bmsgroup.kafkastreamproba.model.BmsOperation;
import ru.bmsgroup.kafkastreamproba.model.BmsPurchase;
import ru.bmsgroup.kafkastreamproba.model.BmsSimpleOperation;
import ru.bmsgroup.kafkastreamproba.model.Identity;
import ru.bmsgroup.kafkastreamproba.model.terminal.TerminalOperation;
import ru.bmsgroup.kafkastreamproba.model.terminal.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.operations.OperationFinder;
import ru.bmsgroup.kafkastreamproba.operations.PurchaseFinder;
import ru.bmsgroup.kafkastreamproba.strategy.BmsStrategy;

import java.util.List;
import java.util.stream.Collectors;

public class ActionToPurchaseBmsStrategy implements BmsStrategy {
    private PurchaseFinder purchaseFinder;
    private OperationFinder operationFinder;

    public ActionToPurchaseBmsStrategy(PurchaseFinder purchaseFinder, OperationFinder operationFinder) {
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
                .identity(Identity.builder()
                        .purchaseId(terminalOperation.parentRrn)
                        .operationId(bmsOperation.getOperationId())
                        .actionId(terminalOperation.currentRrn)
                        .operation(bmsOperation.getOperation())
                        .build())
                .bmsOperation(operation);
    }
}
