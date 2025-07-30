package com.example.transaction_producer.service;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

import com.example.avro.Transaction;
import com.example.transaction_producer.repository.TransactionRepository;
import com.example.transaction_producer.TransactionStatus;
import com.example.transaction_producer.model.TransactionJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
  
  @Autowired
  private KafkaTemplate<String, Object> kafkaTemplate;
  @Autowired
  private TransactionRepository transactionRepository;

  @Scheduled(fixedRate = 15000) // Generate data every 5 seconds
  public void generateMockTransaction() {
//    // Create a map to represent the transaction
//    Map<String, Object> transaction = new HashMap<>();
//    transaction.put("order_id", UUID.randomUUID().toString());
//    transaction.put("user_id", new Random().nextInt(1000));
//    transaction.put("amount", new Random().nextDouble() * 15_000_000); // Up to 15 million
//    transaction.put("status", TransactionStatus.fromCode(new Random().nextInt(3)).name()); // 0=PENDING, 1=SUCCESS, 2=FAILED
//    transaction.put("timestamp", Instant.now().toEpochMilli());
//
//    kafkaTemplate.send("transactions-topic", transaction);

    Transaction transaction = Transaction.newBuilder()
        .setOrderId(UUID.randomUUID().toString())
        .setUserId(new Random().nextInt(1000))
        .setAmount(new Random().nextDouble() * 15_000_000)
        .setStatus(TransactionStatus.fromCode(new Random().nextInt(3)).status)
        .setTimestamp(Instant.now())
        .build();

    TransactionJPA transactionJPA = new TransactionJPA();
    transactionJPA.setOrderId(transaction.getOrderId());
    transactionJPA.setUserId(Long.valueOf(transaction.getUserId()));
    transactionJPA.setAmount(transaction.getAmount());
    transactionJPA.setStatus(transaction.getStatus().name());
    transactionJPA.setTimestamp(transaction.getTimestamp().toEpochMilli());
    transactionRepository.save(transactionJPA);

    kafkaTemplate.send("transactions-topic", transaction);

    System.out.println("Sent transaction: " + transaction);
  }
}