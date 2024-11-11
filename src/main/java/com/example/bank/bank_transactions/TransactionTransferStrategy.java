package com.example.bank.bank_transactions;

import com.example.bank.bank_transactions.model.TransactionEntity;
import com.example.bank.bank_transactions.model.dto.TransactionDepositDto;
import com.example.bank.bank_transactions.model.dto.TransactionLoggerMessageDto;
import com.example.bank.bank_transactions.model.dto.TransactionTransferDto;
import com.example.bank.bank_transactions.model.enums.TransactionAction;
import com.example.bank.bank_transactions.model.enums.TransactionType;
import com.example.bank.bank_transactions.repository.TransactionRepository;
import com.example.bank.bank_transactions.service.TransactionsService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public class TransactionTransferStrategy implements TransactionStrategy<TransactionTransferDto> {

    private final TransactionRepository transactionRepository;
    private final TransactionsService transactionsService;

    public TransactionTransferStrategy(TransactionRepository transactionRepository, TransactionsService transactionsService) {
        this.transactionRepository = transactionRepository;
        this.transactionsService = transactionsService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createTransaction(TransactionTransferDto transactionData) {
        TransactionEntity increaseTransaction = new TransactionEntity();
        TransactionEntity decreaseTransaction = new TransactionEntity();
        increaseTransaction.setTransactionType(TransactionType.TRANSFER);
        decreaseTransaction.setTransactionType(TransactionType.TRANSFER);
        increaseTransaction.setTransactionAction(TransactionAction.INCREASE);
        decreaseTransaction.setTransactionAction(TransactionAction.DECREASE);
        increaseTransaction.setAmount(transactionData.getAmount());
        decreaseTransaction.setAmount(transactionData.getAmount());
        increaseTransaction.setAccount(transactionData.getDestinationAccount());
        decreaseTransaction.setAccount(transactionData.getSourceAccount());
        increaseTransaction.setCreatedAt(LocalDateTime.now());
        decreaseTransaction.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(increaseTransaction);
        transactionRepository.save(decreaseTransaction);

        TransactionLoggerMessageDto increaseLogMessage = new TransactionLoggerMessageDto(increaseTransaction);
        TransactionLoggerMessageDto decreaseLogMessage = new TransactionLoggerMessageDto(decreaseTransaction);
        transactionsService.notifyObservers(increaseLogMessage);
        transactionsService.notifyObservers(decreaseLogMessage);
        return true;

    }

}



