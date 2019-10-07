package ru.bmsgroup.kafkastreamproba.strategy.id;

import ru.bmsgroup.kafkastreamproba.model.BmsOperationType;
import ru.bmsgroup.kafkastreamproba.model.Identity;
import ru.bmsgroup.kafkastreamproba.model.terminal.TerminalOperation;
import ru.bmsgroup.kafkastreamproba.model.terminal.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.strategy.BmsStrategy;

import java.util.List;

public class PurchaseBmsStrategy implements BmsStrategy {
    private BmsOperationType operationType;

    public PurchaseBmsStrategy(BmsOperationType operation) {
        this.operationType = operation;
    }

    @Override
    public List<TransformedOperation.TransformedOperationBuilder> get(TerminalOperation.Identity operation) {
        return List.of(TransformedOperation.builder()
                .identity(Identity.builder()
                        .purchaseId(operation.currentRrn)
                        .operationId(operation.currentRrn)
                        .actionId(operation.currentRrn)
                        .operation(operationType)
                        .build())
                .bmsOperation(null) /*новая операция*/
                .bmsPurchase(null)
        );
    }
}
