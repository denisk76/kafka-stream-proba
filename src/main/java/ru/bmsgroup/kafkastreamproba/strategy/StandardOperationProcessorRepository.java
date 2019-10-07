package ru.bmsgroup.kafkastreamproba.strategy;

import ru.bmsgroup.kafkastreamproba.model.terminal.TerminalOperationType;
import ru.bmsgroup.kafkastreamproba.operations.OperationFinder;
import ru.bmsgroup.kafkastreamproba.operations.PurchaseFinder;
import ru.bmsgroup.kafkastreamproba.strategy.processor.CancelOperationProcessor;
import ru.bmsgroup.kafkastreamproba.strategy.processor.CashOperationProcessor;
import ru.bmsgroup.kafkastreamproba.strategy.processor.ConfirmOperationProcessor;
import ru.bmsgroup.kafkastreamproba.strategy.processor.PaymentOperationProcessor;

public class StandardOperationProcessorRepository implements OperationProcessorRepository {
    private PurchaseFinder purchaseFinder;
    private OperationFinder operationFinder;

    public StandardOperationProcessorRepository(PurchaseFinder purchaseFinder, OperationFinder operationFinder) {
        this.purchaseFinder = purchaseFinder;
        this.operationFinder = operationFinder;
    }

    @Override
    public OperationProcessor get(TerminalOperationType type) {
        switch (type) {
            case PAYMENT:
                return new PaymentOperationProcessor();
            case CONFIRM:
                return new ConfirmOperationProcessor(operationFinder);
            case CANCEL:
                return new CancelOperationProcessor(purchaseFinder, operationFinder);
            case CASH:
                return new CashOperationProcessor(purchaseFinder);
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
