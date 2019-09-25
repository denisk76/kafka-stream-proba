package ru.bmsgroup.kafkastreamproba.operations.transforms;

import ru.bmsgroup.kafkastreamproba.model.*;
import ru.bmsgroup.kafkastreamproba.operations.OperationTransformer;

import java.math.BigDecimal;

public class CashOperationTransformer extends AbstractOperationTransformer implements OperationTransformer {
    @Override
    public BmsTransaction get() {
        purchase = purchaseFinder.get(operation.getPurchaseId());
        return BmsTransaction.builder()
                .build();
    }

    @Override
    protected BmsPurchase getStorePurchase() {
        return null;
    }

    @Override
    protected BmsOperation getStoreOperation() {
        return null;
    }

    @Override
    protected BigDecimal getCalculation() {
        return null;
    }

    @Override
    protected BigDecimal getLocked() {
        return null;
    }

    @Override
    protected BigDecimal getAmount() {
        return null;
    }

    @Override
    protected BmsOperationType getOperation() {
        return null;
    }

    @Override
    protected BmsActionType getActionType() {
        return null;
    }

    @Override
    protected String getOperationId() {
        return null;
    }

    @Override
    protected String getPurchaseId() {
        return null;
    }
}
