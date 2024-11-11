package com.example.bank.config;

import com.example.bank.bank_transactions.TransactionDepositStrategy;
import com.example.bank.bank_transactions.TransactionTransferStrategy;
import com.example.bank.bank_transactions.TransactionWithdrawStrategy;
import com.example.bank.bank_transactions.repository.TransactionRepository;
import com.example.bank.bank_transactions.service.TransactionsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class TransactionStrategyConfig {

    private final TransactionRepository transactionRepository;
    private final TransactionsService transactionsService;

    public TransactionStrategyConfig(TransactionRepository transactionRepository, TransactionsService transactionsService) {
        this.transactionRepository = transactionRepository;
        this.transactionsService = transactionsService;
    }

    @Bean
    @Lazy
    public TransactionDepositStrategy transactionDepositStrategy() {
        return new TransactionDepositStrategy(transactionRepository,transactionsService);
    }

    @Bean
    @Lazy
    public TransactionTransferStrategy transactionTransferStrategy() {
        return new TransactionTransferStrategy(transactionRepository,transactionsService);
    }

    @Bean
    @Lazy
    public TransactionWithdrawStrategy transactionWithdrawStrategy() {
        return new TransactionWithdrawStrategy(transactionRepository,transactionsService);
    }
}
