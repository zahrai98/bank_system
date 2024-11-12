package com.example.bank.bank_transactions.model.dto;

import com.example.bank.bank_transactions.model.TransactionEntity;
import com.example.bank.bank_transactions.model.enums.TransactionAction;
import com.example.bank.bank_transactions.model.enums.TransactionStatus;
import com.example.bank.bank_transactions.model.enums.TransactionType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionLoggerMessageDto {
    private Long transactionId;
    private Long accountId;
    private TransactionAction transactionAction;
    private TransactionType transactionType;
    private Integer amount;
    private LocalDateTime createdAt;
    private TransactionStatus transactionStatus;

    public TransactionLoggerMessageDto(TransactionEntity transactionEntity, Long accountId, TransactionStatus transactionStatus) {
        this.transactionId = transactionEntity.getId();
        this.transactionAction = transactionEntity.getTransactionAction();
        this.transactionType = transactionEntity.getTransactionType();
        this.amount = transactionEntity.getAmount();
        this.createdAt = transactionEntity.getCreatedAt();
        this.transactionStatus = transactionStatus;
        this.accountId = accountId;
    }

    public TransactionLoggerMessageDto(Long accountId, TransactionAction transactionAction,
                                       TransactionType transactionType, Integer amount, LocalDateTime createdAt,
                                       TransactionStatus transactionStatus) {
        this.accountId = accountId;
        this.transactionAction = transactionAction;
        this.transactionType = transactionType;
        this.amount = amount;
        this.createdAt = createdAt;
        this.transactionStatus = transactionStatus;
    }

}
