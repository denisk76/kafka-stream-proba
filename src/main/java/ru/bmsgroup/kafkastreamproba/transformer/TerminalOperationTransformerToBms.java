package ru.bmsgroup.kafkastreamproba.transformer;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bmsgroup.kafkastreamproba.model.BmsTransaction;
import ru.bmsgroup.kafkastreamproba.model.TransformedOperation;
import ru.bmsgroup.kafkastreamproba.strategy.AmountStrategy;
import ru.bmsgroup.kafkastreamproba.strategy.AmountStrategyFactory;
import ru.bmsgroup.kafkastreamproba.strategy.StandardAmountStrategyFactory;

public class TerminalOperationTransformerToBms implements Transformer<String, TransformedOperation, KeyValue<String, BmsTransaction>> {
    public static final Logger LOG = LoggerFactory.getLogger(TerminalOperationTransformerToBms.class);
    private ProcessorContext context;
    private AmountStrategyFactory amountStrategyFactory;

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
        amountStrategyFactory = new StandardAmountStrategyFactory();
    }

    @Override
    public KeyValue<String, BmsTransaction> transform(String key, TransformedOperation operation) {
        AmountStrategy amountStrategy = amountStrategyFactory.get(operation.getData().operation);
        LOG.info("amount strategy by " + operation.getData().operation + " is " + amountStrategy.getClass().getName());
        return KeyValue.pair(key, BmsTransaction.builder()
                .identity(operation.getIdentity())
                .money(amountStrategy.get(operation))
                .build());
    }

    @Override
    public void close() {

    }
}
