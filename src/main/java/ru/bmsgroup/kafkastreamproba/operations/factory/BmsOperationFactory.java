package ru.bmsgroup.kafkastreamproba.operations.factory;

import lombok.Builder;
import ru.bmsgroup.kafkastreamproba.model.TerminalOperationType;
import ru.bmsgroup.kafkastreamproba.operations.OperationFactory;
import ru.bmsgroup.kafkastreamproba.operations.OperationFinder;
import ru.bmsgroup.kafkastreamproba.operations.OperationTransformer;
import ru.bmsgroup.kafkastreamproba.operations.PurchaseFinder;
import ru.bmsgroup.kafkastreamproba.operations.transforms.*;

import java.math.BigDecimal;

@Builder
public class BmsOperationFactory implements OperationFactory {
    private OperationFinder operationFinder;
    private PurchaseFinder purchaseFinder;
    private String currentRrn;
    private String parentRrn;
    private String clientId;
    private BigDecimal amount;

    @Override
    public OperationTransformer get(TerminalOperationType type) {
        AbstractOperationTransformer transformer;
        switch (type) {
            case PAYMENT:
                PaymentOperationTransformer paymentOperationTransformer = new PaymentOperationTransformer();
                transformer = paymentOperationTransformer;
                break;
            case CONFIRM:
                ConfirmOperationTransformer confirmOperationTransformer = new ConfirmOperationTransformer();
                transformer = confirmOperationTransformer;
                break;
            case CANCEL:
                CancelOperationTransformer cancelOperationTransformer = new CancelOperationTransformer();
                transformer = cancelOperationTransformer;
                break;
            case CASH:
                CashOperationTransformer cashOperationTransformer = new CashOperationTransformer();
                transformer = cashOperationTransformer;
                break;
            default:
                throw new RuntimeException("unknown operation: " + type);
        }
        transformer.setCurrentRrn(currentRrn);
        transformer.setParentRrn(parentRrn);
        transformer.setClientId(clientId);
        transformer.setAmount(amount);
        transformer.setOperationFinder(operationFinder);
        transformer.setPurchaseFinder(purchaseFinder);
        return transformer;
    }
}
