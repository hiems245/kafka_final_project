package com.example.transaction_consumer.service;

import com.example.avro.Transaction;
import com.example.transaction_consumer.model.FraudTransaction;
import com.example.transaction_consumer.repository.FraudTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FraudTransactionService {

  @Autowired
  private FraudTransactionRepository fraudTransactionRepository;

  public FraudTransaction save(Transaction transaction) {
    FraudTransaction fraudTransaction = new FraudTransaction();
    fraudTransaction.setOrderId(transaction.getOrderId());
    fraudTransaction.setUserId(Long.valueOf(transaction.getUserId()));
    fraudTransaction.setAmount(transaction.getAmount());
    fraudTransaction.setStatus(transaction.getStatus().name());
    fraudTransaction.setTimestamp(transaction.getTimestamp().toEpochMilli());
    return fraudTransactionRepository.save(fraudTransaction);
  }
}
