package com.example.transaction_consumer.service;

import com.example.avro.Transaction;
import com.example.transaction_consumer.model.FraudTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class FraudDetectionService {

  @Autowired
  private FraudTransactionService fraudTransactionService;

  @KafkaListener(topics = "transactions-topic", groupId = "fraud-detection-group")
  public void checkFraud(Transaction transaction) {
    System.out.println("Transaction received: " + transaction);
    if (transaction.getAmount() > 10_000_000) {
      // Simpan ke database/log atau kirim notifikasi
      FraudTransaction fraudTransaction = fraudTransactionService.save(transaction);

      System.out.println("🚨 Fraud Detected: " + fraudTransaction);
    }
  }
}