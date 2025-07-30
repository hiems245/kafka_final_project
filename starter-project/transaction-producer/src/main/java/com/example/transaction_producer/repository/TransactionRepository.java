package com.example.transaction_producer.repository;

import com.example.transaction_producer.model.TransactionJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionJPA, Long> {
}
