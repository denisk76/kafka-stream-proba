package ru.bmsgroup.kafkastreamproba.transformer;

import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bmsgroup.kafkastreamproba.model.BmsPurchase;
import ru.bmsgroup.kafkastreamproba.model.BmsTransaction;

import java.util.Objects;

public class BmsPurchaseTransformer implements ValueTransformer<BmsTransaction, BmsTransaction> {
    public static final Logger LOG = LoggerFactory.getLogger(BmsPurchaseTransformer.class);
    private KeyValueStore<String, BmsPurchase> purchaseStore;
    private final String purchaseStoreName;
    private ProcessorContext context;

    public BmsPurchaseTransformer(String purchaseStoreName) {
        Objects.requireNonNull(purchaseStoreName,"Purchase Store Name can't be null");
        this.purchaseStoreName = purchaseStoreName;
    }

    @Override
    public void init(ProcessorContext processorContext) {
        this.context = processorContext;
        purchaseStore = (KeyValueStore) this.context.getStateStore(purchaseStoreName);
    }

    @Override
    public BmsTransaction transform(BmsTransaction bmsTransaction) {
        LOG.info("purchase id = "+bmsTransaction.getPurchaseId());
        BmsPurchase bmsPurchase = purchaseStore.get(bmsTransaction.getPurchaseId());
        if(bmsPurchase == null) {
            LOG.info("purchase is null");
            purchaseStore.put(bmsTransaction.getPurchaseId(), BmsPurchase.newBmsPurchase(bmsTransaction));
        } else {
            LOG.info("purchase: " + bmsPurchase);
            purchaseStore.put(bmsTransaction.getPurchaseId(), BmsPurchase.sum(BmsPurchase.newBmsPurchase(bmsTransaction), bmsPurchase));
        }
        return bmsTransaction;
    }

    @Override
    public void close() {

    }
}
