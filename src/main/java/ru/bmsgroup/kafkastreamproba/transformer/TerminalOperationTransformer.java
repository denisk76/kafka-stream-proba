package ru.bmsgroup.kafkastreamproba.transformer;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import ru.bmsgroup.kafkastreamproba.model.BmsOperation;
import ru.bmsgroup.kafkastreamproba.model.BmsPurchase;
import ru.bmsgroup.kafkastreamproba.model.terminal.TerminalOperation;
import ru.bmsgroup.kafkastreamproba.model.terminal.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.strategy.OperationProcessor;
import ru.bmsgroup.kafkastreamproba.strategy.StandardOperationProcessorRepository;

import java.util.Objects;

public class TerminalOperationTransformer implements Transformer<String, TerminalOperation, KeyValue<String, TransformedOperation>> {
    public static final Logger LOG = LoggerFactory.getLogger(TerminalOperationTransformer.class);
    private KeyValueStore<String, BmsOperation> operationStore;
    private KeyValueStore<String, BmsPurchase> purchaseStore;
    private final String operationStoreName;
    private final String purchaseStoreName;
    private ProcessorContext context;
    private StandardOperationProcessorRepository processorRepository;

    public TerminalOperationTransformer(String operationStoreName, String purchaseStoreName) {
        Objects.requireNonNull(operationStoreName, "Operation Store Name can't be null");
        this.operationStoreName = operationStoreName;
        Objects.requireNonNull(purchaseStoreName, "Purchase Store Name can't be null");
        this.purchaseStoreName = purchaseStoreName;
    }

    @Override
    public void init(ProcessorContext processorContext) {
        this.context = processorContext;
        operationStore = (KeyValueStore) this.context.getStateStore(operationStoreName);
        Assert.notNull(operationStore, "operationStore " + operationStoreName + " can't be null");
        purchaseStore = (KeyValueStore) this.context.getStateStore(purchaseStoreName);
        Assert.notNull(purchaseStore, "purchaseStore " + purchaseStoreName + " can't be null");
        processorRepository = new StandardOperationProcessorRepository((id) -> purchaseStore.get(id), (id) -> operationStore.get(id));
    }

    @Override
    public KeyValue<String, TransformedOperation> transform(String key, TerminalOperation terminalOperation) {
        OperationProcessor operationProcessor = processorRepository.get(terminalOperation.getOperation());
        LOG.info("bms strategy by " + terminalOperation.getOperation() + " is " + operationProcessor.getBmsStrategy().getClass().getName());
        operationProcessor.getBmsStrategy().get(terminalOperation.getIdentity()).forEach(t -> context.forward(key,
                t
                        .data(terminalOperation.getTerminalOperationData())
                        .build())
        );
        return null;
    }

    @Override
    public void close() {

    }
}
