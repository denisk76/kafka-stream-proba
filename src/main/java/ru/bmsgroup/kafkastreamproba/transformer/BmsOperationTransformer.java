package ru.bmsgroup.kafkastreamproba.transformer;

import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bmsgroup.kafkastreamproba.model.*;

import java.math.BigDecimal;
import java.util.Objects;

public class BmsOperationTransformer implements ValueTransformer<BmsTransaction, BmsTransaction> {
    public static final Logger LOG = LoggerFactory.getLogger(BmsOperationTransformer.class);
    private KeyValueStore<String, BmsOperation> operationStore;
    private final String operationStoreName;
    private ProcessorContext context;

    public BmsOperationTransformer(String operationStoreName) {
        Objects.requireNonNull(operationStoreName,"Operation Store Name can't be null");
        this.operationStoreName = operationStoreName;
    }

    @Override
    public void init(ProcessorContext processorContext) {
        this.context = processorContext;
        operationStore = (KeyValueStore) this.context.getStateStore(operationStoreName);
    }

    @Override
    public BmsTransaction transform(BmsTransaction bmsTransaction) {
        LOG.info("operation id = "+bmsTransaction.getOperationId());
        BmsOperation bmsOperation = operationStore.get(bmsTransaction.getOperationId());
        if(bmsOperation == null) {
            LOG.info("operation is null");
            operationStore.put(bmsTransaction.getOperationId(), BmsOperation.newBmsOperation(bmsTransaction));
        } else {
            LOG.info("operation: " + bmsOperation);
            operationStore.put(bmsTransaction.getOperationId(), BmsOperation.sum(BmsOperation.newBmsOperation(bmsTransaction), bmsOperation));
        }
        return bmsTransaction;
    }

    @Override
    public void close() {

    }
}
