package ru.bmsgroup.kafkastreamproba.strategy.id;

import ru.bmsgroup.kafkastreamproba.model.BmsOperationType;
import ru.bmsgroup.kafkastreamproba.model.BmsPurchase;
import ru.bmsgroup.kafkastreamproba.model.Identity;
import ru.bmsgroup.kafkastreamproba.model.terminal.TerminalOperation;
import ru.bmsgroup.kafkastreamproba.model.terminal.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.operations.PurchaseFinder;
import ru.bmsgroup.kafkastreamproba.strategy.BmsStrategy;

import java.util.List;

public class OperationBmsStrategy implements BmsStrategy {
    private PurchaseFinder purchaseFinder;
    private BmsOperationType operationType;

    public OperationBmsStrategy(PurchaseFinder purchaseFinder, BmsOperationType operationType) {
        this.purchaseFinder = purchaseFinder;
        this.operationType = operationType;
    }

    @Override
    public List<TransformedOperation.TransformedOperationBuilder> get(TerminalOperation.Identity operation) {
        BmsPurchase bmsPurchase = purchaseFinder.get(operation.parentRrn);
        return List.of(TransformedOperation.builder()
                .identity(Identity.builder()
                        .purchaseId(operation.parentRrn)
                        .operationId(operation.currentRrn)
                        .actionId(operation.currentRrn)
                        .operation(operationType)
                        .build())
                .bmsOperation(null) /*новая операция*/
                .bmsPurchase(bmsPurchase)
        );
    }
}
