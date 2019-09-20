package ru.bmsgroup.kafkastreamproba.strategy.id;

import ru.bmsgroup.kafkastreamproba.model.BmsPurchase;
import ru.bmsgroup.kafkastreamproba.model.BmsTransaction;
import ru.bmsgroup.kafkastreamproba.model.TerminalOperation;
import ru.bmsgroup.kafkastreamproba.model.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.operations.PurchaseFinder;
import ru.bmsgroup.kafkastreamproba.strategy.BmaStrategy;

import java.util.List;

public class OperationBmaStrategy implements BmaStrategy {
    private PurchaseFinder purchaseFinder;

    public OperationBmaStrategy(PurchaseFinder purchaseFinder) {
        this.purchaseFinder = purchaseFinder;
    }

    @Override
    public List<TransformedOperation.TransformedOperationBuilder> get(TerminalOperation.Identity operation) {
        BmsPurchase bmsPurchase = purchaseFinder.get(operation.parentRrn);
        return List.of(TransformedOperation.builder()
                .identity(BmsTransaction.Identity.builder()
                        .purchaseId(operation.parentRrn)
                        .operationId(operation.currentRrn)
                        .actionId(operation.currentRrn)
                        .build())
                .bmsOperation(null) /*новая операция*/
        );
    }
}
