package com.example.kafka_streams.config;

import com.example.avro.Transaction;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SerdesConfig {

    @Bean
    public Serde<Transaction> transactionSerde() {
        SpecificAvroSerde<Transaction> serde = new SpecificAvroSerde<>();
        Map<String, String> config = Map.of("schema.registry.url", "http://localhost:8081");
        serde.configure(config, false); // false = for value
        return serde;
    }
}