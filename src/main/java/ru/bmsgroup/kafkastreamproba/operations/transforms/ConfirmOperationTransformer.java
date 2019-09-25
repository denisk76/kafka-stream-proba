package ru.bmsgroup.kafkastreamproba.operations.transforms;

import ru.bmsgroup.kafkastreamproba.model.BmsActionType;
import ru.bmsgroup.kafkastreamproba.model.BmsOperation;
import ru.bmsgroup.kafkastreamproba.model.BmsOperationType;
import ru.bmsgroup.kafkastreamproba.model.BmsPurchase;
import ru.bmsgroup.kafkastreamproba.operations.OperationTransformer;

import java.math.BigDecimal;

public class ConfirmOperationTransformer extends AbstractOperationTransformer implements OperationTransformer {
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
