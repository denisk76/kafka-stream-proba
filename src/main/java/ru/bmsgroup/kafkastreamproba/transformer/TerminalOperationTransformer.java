package ru.bmsgroup.kafkastreamproba.transformer;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bmsgroup.kafkastreamproba.model.*;
import ru.bmsgroup.kafkastreamproba.operations.OperationFactory;
import ru.bmsgroup.kafkastreamproba.operations.OperationTransformer;
import ru.bmsgroup.kafkastreamproba.operations.factory.BmsOperationFactory;
import ru.bmsgroup.kafkastreamproba.strategy.BmaStrategy;
import ru.bmsgroup.kafkastreamproba.strategy.BmsStrategyFactory;
import ru.bmsgroup.kafkastreamproba.strategy.StandardBmsStrategyFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class TerminalOperationTransformer implements Transformer<String, TerminalOperation, KeyValue<String, TransformedOperation>> {
    public static final Logger LOG = LoggerFactory.getLogger(TerminalOperationTransformer.class);
    private KeyValueStore<String, BmsOperation> operationStore;
    private KeyValueStore<String, BmsPurchase> purchaseStore;
    private final String operationStoreName;
    private final String purchaseStoreName;
    private ProcessorContext context;
    private BmsStrategyFactory bmsStrategyFactory;

    public TerminalOperationTransformer(String operationStoreName, String purchaseStoreName) {
        Objects.requireNonNull(operationStoreName,"Operation Store Name can't be null");
        this.operationStoreName = operationStoreName;
        Objects.requireNonNull(purchaseStoreName,"Purchase Store Name can't be null");
        this.purchaseStoreName = purchaseStoreName;
    }

    @Override
    public void init(ProcessorContext processorContext) {
        this.context = processorContext;
        operationStore = (KeyValueStore) this.context.getStateStore(operationStoreName);
        purchaseStore = (KeyValueStore) this.context.getStateStore(purchaseStoreName);
        bmsStrategyFactory = new StandardBmsStrategyFactory((id) -> purchaseStore.get(id), (id) -> operationStore.get(id));
    }

    public KeyValue<String, BmsTransaction> newTransform(String key, TerminalOperation terminalOperation) {
        OperationFactory operationFactory = BmsOperationFactory.builder()
                .operationFinder((id) -> operationStore.get(id))
                .purchaseFinder((id) -> purchaseStore.get(id))
                .currentRrn(terminalOperation.getCurrentRrn())
                .parentRrn(terminalOperation.getParentRrn())
                .clientId(terminalOperation.getClientId())
                .amount(terminalOperation.getAmount())
                .build();
        OperationTransformer operationTransformer = operationFactory.get(terminalOperation.getOperation());
        List<BmsTransaction> bmsTransactions = operationTransformer.get();
        bmsTransactions.forEach(t -> context.forward(key, t));
        return null;
    }

    @Override
    public KeyValue<String, TransformedOperation> transform(String key, TerminalOperation terminalOperation) {
        BmaStrategy bmaStrategy = bmsStrategyFactory.get(terminalOperation.getOperation());
        LOG.info("bms strategy by " + terminalOperation.getOperation() + " is " + bmaStrategy.getClass().getName());
        bmaStrategy.get(terminalOperation.getIdentity()).forEach(t -> context.forward(key,
                t.data(terminalOperation.getTerminalOperationData()).build())
        );
        return null;
    }

    public KeyValue<String, BmsTransaction> transform2(String key, TerminalOperation terminalOperation) {
        BmsPurchase bmsPurchase = null;
        BmsOperation bmsOperation = null;
        switch (terminalOperation.getOperation()) {
            case PAYMENT:
                /*Пришёл платёж, истории ещё нет*/
                break;
            case CASH:
                /*Пришёл кешбэк на уже существующий платёж*/
                bmsPurchase = purchaseStore.get(terminalOperation.getParentRrn());
                break;
            case CONFIRM:
            case CANCEL:
                /*Подтверждение или отмена уже существующих операций*/
                bmsOperation = operationStore.get(terminalOperation.getParentRrn());
                bmsPurchase = purchaseStore.get(bmsOperation.getPurchaseId());
                break;
        }
        LOG.info("Преобразование терминальной операции "+terminalOperation.getOperation());
        LOG.info("purchase: " + bmsPurchase);
        LOG.info("operation: " + bmsOperation);
        /* Если это отмена всей покупки, то отменяем все операции, входящие в эту покупку.*/
        if(terminalOperation.getOperation().equals(TerminalOperationType.CANCEL) && bmsPurchase != null) {
            for (BmsSimpleOperation operation : bmsPurchase.getOperations()) {
                bmsOperation = operationStore.get(operation.getOperationId());
                context.forward(key,
                        BmsTransaction.builder()
                                .clientId(terminalOperation.getClientId())
                                .purchaseId(bmsPurchase.getPurchaseId())
                                .operationId(operation.getOperationId())
                                .operation(operation.getOperation())
                                .actionId(terminalOperation.getCurrentRrn())       /* новый action*/
                                .action(BmsActionType.CANCEL)                       /* отменяем операцию*/
                                .amount(bmsOperation.getAmount())
                                .locked(bmsOperation.getLocked())
                                .calculation(bmsOperation.getCalculation())
                                .build());
            }
            return null;
        } else {
            return  KeyValue.pair(key,BmsTransaction.builder()
                    .clientId(terminalOperation.getClientId())
                    .purchaseId(getPurchaseId(terminalOperation.getParentRrn(), bmsPurchase))
                    .operationId(getOperationId(terminalOperation.getCurrentRrn(), bmsOperation))
                    .operation(convertOperation(terminalOperation.getOperation(), bmsOperation))
                    .actionId(terminalOperation.getCurrentRrn())
                    .action(convertType(terminalOperation.getOperation()))
                    .amount(BigDecimal.ZERO)
                    .locked(getLocked(terminalOperation.getAmount(), terminalOperation.getOperation()))
                    .calculation(getCalculation(terminalOperation.getAmount(), terminalOperation.getOperation()))
                    .build());
        }
    }

    private BigDecimal getLocked(BigDecimal amount, TerminalOperationType type) {
        switch (type) {
            case PAYMENT:
                return BigDecimal.ZERO;
            case CONFIRM:
                return amount;
            case CANCEL:
                return amount;
            case CASH:
                return amount.negate();
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getCalculation(BigDecimal amount, TerminalOperationType type) {
        switch (type) {
            case PAYMENT:
                return amount;
            case CONFIRM:
                return amount.negate();
            case CANCEL:
                return BigDecimal.ZERO;
            case CASH:
                return BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }

    private String getPurchaseId(String id, BmsPurchase bmsPurchase) {
        if(bmsPurchase == null) {
            return id;
        }
        return bmsPurchase.getPurchaseId();
    }

    private String getOperationId(String id, BmsOperation bmsOperation) {
        if(bmsOperation == null) {
            return id;
        }
        return bmsOperation.getOperationId();
    }

    private BmsOperationType convertOperation(TerminalOperationType type, BmsOperation bmsOperation) {
        switch (type) {
            case CONFIRM:
            case CANCEL:
                /*смотрим на исходную операцию*/
                return bmsOperation.getOperation();
            case PAYMENT:
                return BmsOperationType.PAYMENT;
            case CASH:
                return BmsOperationType.CASH;
        }
        return BmsOperationType.PAYMENT;
    }

    private BmsActionType convertType(TerminalOperationType type) {
        switch (type) {
            case PAYMENT:
            case CASH:
                return BmsActionType.NEW;
            case CONFIRM:
                return BmsActionType.CONFIRM;
            case CANCEL:
                return BmsActionType.CANCEL;
        }
        return BmsActionType.NEW;
    }

    @Override
    public void close() {

    }
}
