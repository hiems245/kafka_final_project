package com.example.kafka_streams.config;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.example.avro.HourlyTransactionSummary;
import com.example.avro.Transaction;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonSerde;

@Configuration
public class StreamsConfig {

  @Bean
  public KStream<String, Transaction> kStream(StreamsBuilder streamsBuilder, @Qualifier("transactionSerde") Serde<Transaction> transactionSerde) {
    KStream<String, Transaction> stream = streamsBuilder.stream("transactions-topic",
        Consumed.with(Serdes.String(), transactionSerde));

    stream.peek((key, transaction) -> System.out.println("Incoming transaction: key=" + key + ", value=" + transaction));

    // Create new streams with key
    KStream<String, Transaction> keyedByHourUser = stream.map((key, value) -> {
      String hour = value.getTimestamp()
          .truncatedTo(ChronoUnit.HOURS)
          .toString();
      String compositeKey = hour + "|" + value.getUserId();
      return new KeyValue<>(compositeKey, value);
    });

    keyedByHourUser.peek((key, transaction) -> System.out.println("Converted transaction: key=" + key + ", value=" + transaction));

    KTable<String, Summary> aggregated = keyedByHourUser
        .groupByKey(Grouped.with(Serdes.String(), transactionSerde))
        .aggregate(
            Summary::new,
            (key, tx, summary) -> {
              summary.count += 1;
              summary.totalAmount += tx.getAmount();
              return summary;
            },
            Materialized.with(Serdes.String(), new JsonSerde<>(Summary.class)) // or use Avro if needed
        );

    SpecificAvroSerde<HourlyTransactionSummary> avroSerde = new SpecificAvroSerde<>();
    Map<String, String> serdeConfig = Collections.singletonMap("schema.registry.url", "http://localhost:8081");
    avroSerde.configure(serdeConfig, false); // false = value serde

    Produced<String, HourlyTransactionSummary> produced = Produced.with(Serdes.String(), avroSerde);

    aggregated.toStream()
        .map((compositeKey, summary) -> {
          String[] parts = compositeKey.split("\\|");
          String hour = parts[0];
          int userId = Integer.parseInt(parts[1]);

          HourlyTransactionSummary record = HourlyTransactionSummary.newBuilder()
              .setHour(hour)
              .setUserId(userId)
              .setTransactionCount(summary.count)
              .setTransactionTotalAmount(summary.totalAmount)
              .build();

          System.out.println(compositeKey + record);

          return new KeyValue<>(compositeKey, record);
        })
        .to("hourly-transaction-summary-topic", produced);

    return stream;
  }

  @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
  public KafkaStreamsConfiguration kStreamsConfigs() {
    Map<String, Object> props = new HashMap<>();
    props.put(org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG, "kafka-streams-app");
    props.put(org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
    props.put("schema.registry.url", "http://localhost:8081");

    // Add default SerDes as fallback (optional)
    props.put(org.apache.kafka.streams.StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    props.put(org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Long().getClass());
    props.put(org.apache.kafka.streams.StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
        LogAndContinueExceptionHandler.class);

    return new KafkaStreamsConfiguration(props);
  }

  private static class Summary {
    public int count;
    public double totalAmount;
  }
}