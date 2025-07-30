package com.example.transaction_producer.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "transactions")
@Data
public class TransactionJPA {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "order_id", nullable = false, length = 100)
  private String orderId;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "amount", nullable = false)
  private Double amount;

  @Column(name = "status", nullable = false, length = 100)
  private String status;

  @Column(name = "timestamp", nullable = false)
  private Long timestamp;

}

