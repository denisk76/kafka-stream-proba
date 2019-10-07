package ru.bmsgroup.kafkastreamproba.transformer;

import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import ru.bmsgroup.kafkastreamproba.model.BmsOperation;
import ru.bmsgroup.kafkastreamproba.model.BmsTransaction;

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
        Objects.requireNonNull(operationStore, "Operation Store can't be null");
        LOG.info("Operation Store Found SUCCESS");
    }

    @Override
    public BmsTransaction transform(BmsTransaction bmsTransaction) {
        LOG.info("operation id = "+bmsTransaction.getOperationId());
        Assert.notNull(bmsTransaction.getOperationId(), "operation id can't be null");
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
