package ru.bmsgroup.kafkastreamproba.operations.transforms;

import ru.bmsgroup.kafkastreamproba.model.*;
import ru.bmsgroup.kafkastreamproba.operations.OperationTransformer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Преобразователь Операции отмены
 */
public class CancelOperationTransformer extends AbstractOperationTransformer implements OperationTransformer {
    @Override
    public List<BmsTransaction> get() {
        operation = getStoreOperation();
        purchase = getStorePurchase();
        ArrayList<BmsTransaction> list = new ArrayList<>();
        if (purchase != null) {
            for (BmsSimpleOperation operation : purchase.getOperations()) {
                BmsTransaction transaction = BmsTransaction.builder()
                        .clientId(clientId)
                        .purchaseId(getPurchaseId())
                        .operationId(getOperationId())
                        .actionId(currentRrn)
                        .action(getActionType())
                        .operation(getOperation())
                        .amount(getAmount())
                        .locked(getLocked())
                        .calculation(getCalculation())
                        .build();
                list.add(transaction);
            }
            return list;
        } else {
            return super.get();
        }
    }

    @Override
    protected BmsPurchase getStorePurchase() {
        return purchaseFinder.get(operation.getPurchaseId());
    }

    @Override
    protected BmsOperation getStoreOperation() {
        return operationFinder.get(parentRrn);
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
        return operation.getOperation();
    }

    @Override
    protected BmsActionType getActionType() {
        return BmsActionType.CANCEL;
    }

    @Override
    protected String getOperationId() {
        return parentRrn;
    }

    @Override
    protected String getPurchaseId() {
        return operation.getPurchaseId();
    }
}
