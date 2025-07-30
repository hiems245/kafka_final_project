package com.example.transaction_consumer.repository;

import com.example.transaction_consumer.model.FraudTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FraudTransactionRepository extends JpaRepository<FraudTransaction, Long> {
}
