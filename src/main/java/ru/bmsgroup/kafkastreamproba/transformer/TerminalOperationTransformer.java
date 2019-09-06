package ru.bmsgroup.kafkastreamproba.transformer;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bmsgroup.kafkastreamproba.model.*;

import java.math.BigDecimal;
import java.util.Objects;

public class TerminalOperationTransformer implements Transformer<String, TerminalOperation, KeyValue<String, BmsTransaction>> {
    public static final Logger LOG = LoggerFactory.getLogger(TerminalOperationTransformer.class);
    private KeyValueStore<String, BmsOperation> operationStore;
    private KeyValueStore<String, BmsPurchase> purchaseStore;
    private final String operationStoreName;
    private final String purchaseStoreName;
    private ProcessorContext context;

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
    }

    @Override
    public KeyValue<String, BmsTransaction> transform(String key, TerminalOperation terminalOperation) {
        BmsPurchase bmsPurchase = null;
        BmsOperation bmsOperation = null;
        switch (terminalOperation.getOperation()) {
            case PAYMENT:
                /*Пришёл платёж, истории ещё нет*/
                break;
            case CASH:
                /*Пришёл кешбэк на уже существующий платёж*/
                bmsPurchase = purchaseStore.get(terminalOperation.getPurchaseId());
                break;
            case CONFIRM:
            case CANCEL:
                /*Подтверждение или отмена уже существующих операций*/
                bmsOperation = operationStore.get(terminalOperation.getPurchaseId());
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
                                .actionId(terminalOperation.getOperationId())       /* новый action*/
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
                    .purchaseId(getPurchaseId(terminalOperation.getPurchaseId(), bmsPurchase))
                    .operationId(getOperationId(terminalOperation.getOperationId(), bmsOperation))
                    .operation(convertOperation(terminalOperation.getOperation(), bmsOperation))
                    .actionId(terminalOperation.getOperationId())
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
