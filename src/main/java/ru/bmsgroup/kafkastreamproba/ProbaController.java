package ru.bmsgroup.kafkastreamproba;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreType;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bmsgroup.kafkastreamproba.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProbaController {
    public static final Logger LOG = LoggerFactory.getLogger(ProbaController.class);
    public static final String TRANSACTIONS_STREAM_NAME = "bms-transactions";
    private static Callback callback;
    @Autowired
    KafkaProducer<String, String> producer;
    @Autowired
    StreamsBuilderFactoryBean factoryBean;

    @GetMapping("/send")
    public String send() {
        if (callback == null) {
            callback = (metadata, exception) -> {
                if (exception != null) {
                    exception.printStackTrace();
                }
            };
        }
        sendRecord(BmsTransaction.builder()
                .clientId("1")
                .purchaseId("PRCH-1")
                .operationId("OPRN-1")
                .operation(BmsOperationType.PAYMENT)
                .actionId("A1")
                .action(BmsActionType.NEW)
                .amount(BigDecimal.ZERO)
                .locked(BigDecimal.valueOf(12))
                .calculation(BigDecimal.ZERO)
                .build());
        sendRecord(BmsTransaction.builder()
                .clientId("1")
                .purchaseId("PRCH-1")
                .operationId("OPRN-1")
                .operation(BmsOperationType.PAYMENT)
                .actionId("A2")
                .action(BmsActionType.CONFIRM)
                .amount(BigDecimal.valueOf(12))
                .locked(BigDecimal.valueOf(-12))
                .calculation(BigDecimal.ZERO)
                .build());
        sendRecord(BmsTransaction.builder()
                .clientId("1")
                .purchaseId("PRCH-1")
                .operationId("OPRN-2")
                .operation(BmsOperationType.CASH)
                .actionId("A2")
                .action(BmsActionType.CONFIRM)
                .amount(BigDecimal.valueOf(-2))
                .locked(BigDecimal.valueOf(2))
                .calculation(BigDecimal.ZERO)
                .build());
        sendRecord(BmsTransaction.builder()
                .clientId("1")
                .purchaseId("PRCH-1")
                .operationId("OPRN-2")
                .operation(BmsOperationType.CASH)
                .actionId("A2")
                .action(BmsActionType.CONFIRM)
                .amount(BigDecimal.ZERO)
                .locked(BigDecimal.valueOf(-2))
                .calculation(BigDecimal.ZERO)
                .build());
        LOG.info("Test send");
        return "Hello, world";
    }

    private void sendRecord(BmsTransaction transaction1) {
        Gson gson = new Gson();
//        ProducerRecord<String, String> record = new ProducerRecord<>(TRANSACTIONS_STREAM_NAME, null, "{\"clientId\":\"1\",\"name\":\"Pushkin\"}");
        ProducerRecord<String, String> record = new ProducerRecord<>(TRANSACTIONS_STREAM_NAME, null, gson.toJson(transaction1));
        producer.send(record, callback);
    }

    @GetMapping("/showOperations")
    public String showTable() {
        LOG.info("Test show operations");
        ReadOnlyKeyValueStore<String, BmsOperation> store = factoryBean.getKafkaStreams().store(KafkaStreamsConfiguration.OPERATIONS_STORE_NAME, QueryableStoreTypes.keyValueStore());
        List<KeyValue<String, BmsOperation>> list = new ArrayList<>();
        try(KeyValueIterator<String, BmsOperation> all = store.all()) {
            while (all.hasNext()) {
                list.add(all.next());
            }
        }
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @GetMapping("/showPurchases")
    public String showPurchases() {
        LOG.info("Test show purchase");
        ReadOnlyKeyValueStore<String, BmsPurchase> store = factoryBean.getKafkaStreams().store(KafkaStreamsConfiguration.PURCHASES_STORE_NAME, QueryableStoreTypes.keyValueStore());
        List<KeyValue<String, BmsPurchase>> list = new ArrayList<>();
        try(KeyValueIterator<String, BmsPurchase> all = store.all()) {
            while (all.hasNext()) {
                list.add(all.next());
            }
        }
        Gson gson = new Gson();
        return gson.toJson(list);
    }

}
