package ru.bmsgroup.kafkastreamproba.strategy.id;

import ru.bmsgroup.kafkastreamproba.model.BmsOperation;
import ru.bmsgroup.kafkastreamproba.model.BmsTransaction;
import ru.bmsgroup.kafkastreamproba.model.TerminalOperation;
import ru.bmsgroup.kafkastreamproba.model.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.operations.OperationFinder;
import ru.bmsgroup.kafkastreamproba.strategy.BmaStrategy;

import java.util.List;

public class ActionToOperationBmaStrategy implements BmaStrategy {
    private OperationFinder operationFinder;

    public ActionToOperationBmaStrategy(OperationFinder operationFinder) {
        this.operationFinder = operationFinder;
    }

    @Override
    public List<TransformedOperation.TransformedOperationBuilder> get(TerminalOperation.Identity operation) {
        BmsOperation bmsOperation = operationFinder.get(operation.parentRrn);
        return List.of(TransformedOperation.builder()
                .identity(BmsTransaction.Identity.builder()
                        .purchaseId(bmsOperation.getPurchaseId())
                        .operationId(operation.parentRrn)
                        .actionId(operation.currentRrn)
                        .build())
                .bmsOperation(bmsOperation)
        );
    }
}
