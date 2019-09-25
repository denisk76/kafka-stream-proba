package ru.bmsgroup.kafkastreamproba;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.bmsgroup.kafkastreamproba.model.BmsOperation;
import ru.bmsgroup.kafkastreamproba.model.BmsPurchase;
import ru.bmsgroup.kafkastreamproba.model.BmsTransaction;
import ru.bmsgroup.kafkastreamproba.model.TerminalOperation;
import ru.bmsgroup.kafkastreamproba.transformer.BmsOperationTransformer;
import ru.bmsgroup.kafkastreamproba.transformer.BmsPurchaseTransformer;
import ru.bmsgroup.kafkastreamproba.transformer.TerminalOperationTransformer;
import ru.bmsgroup.kafkastreamproba.transformer.TerminalOperationTransformerToBms;

import java.util.Properties;

@Configuration
@EnableKafkaStreams
@EnableKafka
public class KafkaStreamsConfiguration {
    public static final Logger LOG = LoggerFactory.getLogger(KafkaStreamsConfiguration.class);
    public static final String TRANSACTIONS_STREAM_NAME = "bms-transactions";
    public static final String OPERATIONS_STORE_NAME = "OPERATIONS_STORE";
    public static final String PURCHASES_STORE_NAME = "PURCHASES_STORE";
    private static Serde<BmsOperation> operationSerde = Serdes.serdeFrom(new JsonSerializer(), new JsonDeserializer(BmsOperation.class));
    private static Serde<BmsPurchase> purchaseSerde = Serdes.serdeFrom(new JsonSerializer(), new JsonDeserializer(BmsPurchase.class));
    private static Serde<TerminalOperation> TerminalOperationSerde = Serdes.serdeFrom(new JsonSerializer(), new JsonDeserializer(TerminalOperation.class));
    private static Serde<String> stringSerde = Serdes.String();

    @Bean
    public KStream<String, BmsTransaction> transactionStream(StreamsBuilder builder) {
        KeyValueMapper<String, BmsTransaction, String> clientIdAsKey = ((key, value) -> value.getClientId());
        KeyValueMapper<String, BmsTransaction, String> operationIdAsKey = ((key, value) -> value.getOperationId());
        KeyValueMapper<String, BmsTransaction, String> purchaseIdAsKey = ((k, v) -> v.getPurchaseId());
        addStore(builder,OPERATIONS_STORE_NAME,operationSerde);
        addStore(builder,PURCHASES_STORE_NAME,purchaseSerde);
        /* формируем actions из терминальных операций*/
        KStream<String, BmsTransaction> stream = builder.stream(TRANSACTIONS_STREAM_NAME, Consumed.with(stringSerde, TerminalOperationSerde))
                .transform(()-> new TerminalOperationTransformer(OPERATIONS_STORE_NAME, PURCHASES_STORE_NAME),OPERATIONS_STORE_NAME, PURCHASES_STORE_NAME)
                .transform(() -> new TerminalOperationTransformerToBms())
                .selectKey(clientIdAsKey);
        stream.print(Printed.<String, BmsTransaction>toSysOut().withLabel("transactions"));
        /* из actions формируем операции*/
        KStream<String, BmsTransaction> operationsStream = stream.selectKey(operationIdAsKey)
                .transformValues(() -> new BmsOperationTransformer(OPERATIONS_STORE_NAME), OPERATIONS_STORE_NAME);
        operationsStream.print(Printed.<String, BmsTransaction>toSysOut().withLabel("operations"));
        /* из actions формируем покупки*/
        KStream<String, BmsTransaction> purchaseStream = stream.selectKey(purchaseIdAsKey)
                .transformValues(() -> new BmsPurchaseTransformer(PURCHASES_STORE_NAME), PURCHASES_STORE_NAME);
        return purchaseStream;
    }

    private void addStore(StreamsBuilder builder, String storeName, Serde serde) {
        KeyValueBytesStoreSupplier operationStoreSupplier = Stores.inMemoryKeyValueStore(storeName);
        StoreBuilder<KeyValueStore<String, BmsOperation>> operationStoreBuilder = Stores.keyValueStoreBuilder(operationStoreSupplier, stringSerde, serde);
        builder.addStateStore(operationStoreBuilder);
    }

    @Bean
    public KafkaProducer<String, String> kafkaProducer() {
        LOG.info("Initializing the producer");
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("acks", "1");
        properties.put("retries", "3");
        return new KafkaProducer<>(properties);
    }
}
