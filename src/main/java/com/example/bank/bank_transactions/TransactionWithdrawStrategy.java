package com.example.bank.bank_transactions;

import com.example.bank.bank_transactions.model.TransactionEntity;
import com.example.bank.bank_transactions.model.dto.TransactionDepositDto;
import com.example.bank.bank_transactions.model.dto.TransactionLoggerMessageDto;
import com.example.bank.bank_transactions.model.dto.TransactionWithdrawDto;
import com.example.bank.bank_transactions.model.enums.TransactionAction;
import com.example.bank.bank_transactions.model.enums.TransactionType;
import com.example.bank.bank_transactions.repository.TransactionRepository;
import com.example.bank.bank_transactions.service.TransactionsService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public class TransactionWithdrawStrategy implements TransactionStrategy<TransactionWithdrawDto> {

    private final TransactionRepository transactionRepository;
    private final TransactionsService transactionsService;

    public TransactionWithdrawStrategy(TransactionRepository transactionRepository, TransactionsService transactionsService) {
        this.transactionRepository = transactionRepository;
        this.transactionsService = transactionsService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createTransaction(TransactionWithdrawDto transactionData) {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setTransactionAction(TransactionAction.DECREASE);
        transaction.setAmount(transactionData.getAmount());
        transaction.setAccount(transactionData.getSourceAccount());
        transaction.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);

        TransactionLoggerMessageDto logMessage = new TransactionLoggerMessageDto(transaction);
        transactionsService.notifyObservers(logMessage);
        return true;

    }

}



