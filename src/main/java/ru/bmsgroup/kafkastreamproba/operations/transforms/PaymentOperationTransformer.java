package ru.bmsgroup.kafkastreamproba.operations.transforms;

import ru.bmsgroup.kafkastreamproba.model.BmsActionType;
import ru.bmsgroup.kafkastreamproba.model.BmsOperation;
import ru.bmsgroup.kafkastreamproba.model.BmsOperationType;
import ru.bmsgroup.kafkastreamproba.model.BmsPurchase;
import ru.bmsgroup.kafkastreamproba.operations.OperationTransformer;

import java.math.BigDecimal;

public class PaymentOperationTransformer extends AbstractOperationTransformer implements OperationTransformer {
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
        return amount;
    }

    @Override
    protected BigDecimal getLocked() {
        return BigDecimal.ZERO;
    }

    @Override
    protected BigDecimal getAmount() {
        return BigDecimal.ZERO;
    }

    @Override
    protected BmsOperationType getOperation() {
        return BmsOperationType.PAYMENT;
    }

    @Override
    protected BmsActionType getActionType() {
        return BmsActionType.NEW;
    }

    @Override
    protected String getOperationId() {
        return currentRrn;
    }

    @Override
    protected String getPurchaseId() {
        return currentRrn;
    }
}
