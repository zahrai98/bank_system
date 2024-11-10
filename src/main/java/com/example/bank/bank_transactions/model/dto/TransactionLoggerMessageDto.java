package com.example.bank.bank_transactions.model.dto;

import com.example.bank.bank_transactions.model.TransactionEntity;
import com.example.bank.bank_transactions.model.enums.TransactionAction;
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
    private TransactionAction transactionAction;
    private TransactionType transactionType;
    private Integer amount;
    private LocalDateTime createdAt;

    public TransactionLoggerMessageDto(TransactionEntity transactionEntity) {
        this.transactionAction = transactionEntity.getTransactionAction();
        this.transactionType = transactionEntity.getTransactionType();
        this.amount = transactionEntity.getAmount();
        this.createdAt = transactionEntity.getCreatedAt();

    }

}
