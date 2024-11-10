package com.example.bank;

import com.example.bank.bank_transactions.service.TransactionLoggerService;
import com.example.bank.bank_transactions.service.TransactionsService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
public class BankApplication implements CommandLineRunner {
    private final TransactionsService transactionService;
    private final TransactionLoggerService transactionLoggerService;

    public BankApplication(TransactionsService transactionService, TransactionLoggerService transactionLoggerService) {
        this.transactionService = transactionService;
        this.transactionLoggerService = transactionLoggerService;
    }

    @Override
    public void run(String... args) throws Exception {
        transactionService.addObserver(transactionLoggerService);
    }

    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
    }


}
