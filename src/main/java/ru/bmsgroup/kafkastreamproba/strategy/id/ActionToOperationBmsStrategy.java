package ru.bmsgroup.kafkastreamproba.strategy.id;

import org.springframework.util.Assert;
import ru.bmsgroup.kafkastreamproba.model.BmsOperation;
import ru.bmsgroup.kafkastreamproba.model.Identity;
import ru.bmsgroup.kafkastreamproba.model.terminal.TerminalOperation;
import ru.bmsgroup.kafkastreamproba.model.terminal.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.operations.OperationFinder;
import ru.bmsgroup.kafkastreamproba.strategy.BmsStrategy;

import java.util.List;

public class ActionToOperationBmsStrategy implements BmsStrategy {
    private OperationFinder operationFinder;

    public ActionToOperationBmsStrategy(OperationFinder operationFinder) {
        this.operationFinder = operationFinder;
    }

    @Override
    public List<TransformedOperation.TransformedOperationBuilder> get(TerminalOperation.Identity operation) {
        Assert.notNull(operation.parentRrn, "parentRrn can't be null");
        BmsOperation bmsOperation = operationFinder.get(operation.parentRrn);
        return List.of(TransformedOperation.builder()
                .identity(Identity.builder()
                        .purchaseId(bmsOperation.getPurchaseId())
                        .operationId(operation.parentRrn)
                        .actionId(operation.currentRrn)
                        .operation(bmsOperation.getOperation())
                        .build())
                .bmsOperation(bmsOperation)
        );
    }
}
