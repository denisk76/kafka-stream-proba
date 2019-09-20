package ru.bmsgroup.kafkastreamproba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
public class KafkaStreamProbaApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaStreamProbaApplication.class, args);
    }

    @Bean
    public MessageListener messageListener() {
        return new MessageListener();
    }

    public static class MessageListener {
        @KafkaListener(topics = "bms-transactions", groupId = "join_driver_group")
        public void listenGroupBar(String message) {
            System.out.println("Received Messasge in group 'join_driver_group': " + message);
        }
    }
}
