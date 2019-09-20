package ru.bmsgroup.kafkastreamproba.operations.transforms;

import ru.bmsgroup.kafkastreamproba.model.BmsTransaction;
import ru.bmsgroup.kafkastreamproba.operations.OperationTransformer;

public class CashOperationTransformer extends AbstractOperationTransformer implements OperationTransformer {
    @Override
    public BmsTransaction get() {
        purchase = purchaseFinder.get(operation.getPurchaseId());
        return BmsTransaction.builder()
                .build();
    }
}
