package com.example.bank.bank_transactions.model.dto;


import com.example.bank.bank_transactions.model.TransactionEntity;
import com.example.bank.bank_transactions.model.enums.TransactionAction;
import com.example.bank.bank_transactions.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class TransactionOutDto {
    private LocalDateTime createdAt;
    private BigDecimal amount;
    private TransactionAction transactionAction;
    private TransactionType transactionType;

    public TransactionOutDto(TransactionEntity transactionEntity) {
        if (transactionEntity != null) {
            this.setCreatedAt(transactionEntity.getCreatedAt());
            this.setAmount(transactionEntity.getAmount());
            this.setTransactionAction(transactionEntity.getTransactionAction());
            this.setTransactionType(transactionEntity.getTransactionType());
        }
    }

}