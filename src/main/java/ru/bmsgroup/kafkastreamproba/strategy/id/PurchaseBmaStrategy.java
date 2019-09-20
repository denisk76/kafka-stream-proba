package ru.bmsgroup.kafkastreamproba.strategy.id;

import ru.bmsgroup.kafkastreamproba.model.BmsTransaction;
import ru.bmsgroup.kafkastreamproba.model.TerminalOperation;
import ru.bmsgroup.kafkastreamproba.model.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.strategy.BmaStrategy;

import java.util.List;

public class PurchaseBmaStrategy implements BmaStrategy {
    @Override
    public List<TransformedOperation.TransformedOperationBuilder> get(TerminalOperation.Identity operation) {
        return List.of(TransformedOperation.builder()
                .identity(BmsTransaction.Identity.builder()
                        .purchaseId(operation.currentRrn)
                        .operationId(operation.currentRrn)
                        .actionId(operation.currentRrn)
                        .build())
                .bmsOperation(null) /*новая операция*/
        );
    }
}
