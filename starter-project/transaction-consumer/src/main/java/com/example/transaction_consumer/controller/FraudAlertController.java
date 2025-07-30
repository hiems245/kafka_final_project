package com.example.transaction_consumer.controller;

import java.util.List;

import com.example.transaction_consumer.model.FraudTransaction;
import com.example.transaction_consumer.repository.FraudTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alerts")
public class FraudAlertController {

  @Autowired
  private FraudTransactionRepository fraudTransactionRepository;
  
  @GetMapping("/fraud")
  public ResponseEntity<List<FraudTransaction>> getRecentFraudAlerts() {
    List<FraudTransaction> fraudTransactions = fraudTransactionRepository.findAll();
    return ResponseEntity.ok(fraudTransactions);
  }
}