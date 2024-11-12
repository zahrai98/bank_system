package com.example.bank.bank_transactions;

import com.example.bank.bank_transactions.model.TransactionEntity;
import com.example.bank.bank_transactions.model.dto.TransactionDepositDto;
import com.example.bank.bank_transactions.model.dto.TransactionLoggerMessageDto;
import com.example.bank.bank_transactions.model.enums.TransactionAction;
import com.example.bank.bank_transactions.model.enums.TransactionStatus;
import com.example.bank.bank_transactions.model.enums.TransactionType;
import com.example.bank.bank_transactions.repository.TransactionRepository;
import com.example.bank.bank_transactions.service.TransactionsService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public class TransactionDepositStrategy implements TransactionStrategy<TransactionDepositDto> {

    private final TransactionRepository transactionRepository;
    private final TransactionsService transactionsService;

    public TransactionDepositStrategy(TransactionRepository transactionRepository, TransactionsService transactionsService) {
        this.transactionRepository = transactionRepository;
        this.transactionsService = transactionsService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void createTransaction(TransactionDepositDto transactionData) {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setTransactionAction(TransactionAction.INCREASE);
        transaction.setAmount(transactionData.getAmount());
        transaction.setAccount(transactionData.getDestinationAccount());
        transaction.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);

        TransactionLoggerMessageDto logMessage = new TransactionLoggerMessageDto(transaction,transactionData.getDestinationAccount().getId(), TransactionStatus.CREATED);
        transactionsService.notifyObservers(logMessage);
    }

}



