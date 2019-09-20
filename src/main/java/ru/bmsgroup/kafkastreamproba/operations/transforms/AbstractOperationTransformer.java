package ru.bmsgroup.kafkastreamproba.operations.transforms;

import lombok.Setter;
import ru.bmsgroup.kafkastreamproba.model.*;
import ru.bmsgroup.kafkastreamproba.operations.OperationFinder;
import ru.bmsgroup.kafkastreamproba.operations.OperationTransformer;
import ru.bmsgroup.kafkastreamproba.operations.PurchaseFinder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Преобразователь терминальной операции
 */
@Setter
public abstract class AbstractOperationTransformer implements OperationTransformer {
    protected OperationFinder operationFinder;
    protected PurchaseFinder purchaseFinder;
    protected String currentRrn;
    protected String parentRrn;
    protected String clientId;
    protected BigDecimal amount;
    protected BmsOperation operation;
    protected BmsPurchase purchase;

    @Override
    public List<BmsTransaction> get() {
        operation = getStoreOperation();
        purchase = getStorePurchase();

        ArrayList<BmsTransaction> list = new ArrayList<>();
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
        return list;
    }

    protected abstract BmsPurchase getStorePurchase();

    protected abstract BmsOperation getStoreOperation();

    protected abstract BigDecimal getCalculation();

    protected abstract BigDecimal getLocked();

    protected abstract BigDecimal getAmount();

    protected abstract BmsOperationType getOperation();

    protected abstract BmsActionType getActionType();

    protected abstract String getOperationId();

    protected abstract String getPurchaseId();
}
