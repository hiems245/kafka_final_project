package com.example.transaction_producer;


import com.example.avro.Status;

public enum TransactionStatus {
  PENDING(0, Status.PENDING),
  SUCCESS(1, Status.SUCCESS),
  FAILED(2, Status.FAILED)
  ;

  public Integer code;
  public Status status;

  TransactionStatus(Integer code, Status status) {
    this.code = code;
    this.status = status;
  }

  public static TransactionStatus fromCode(Integer code) {
    for (TransactionStatus value : TransactionStatus.values()) {
      if (value.code == code) {
        return value;
      }
    }

    return PENDING;
  }
}
