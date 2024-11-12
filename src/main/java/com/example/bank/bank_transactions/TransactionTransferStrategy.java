package com.example.bank.bank_transactions;

import com.example.bank.bank_transactions.model.TransactionEntity;
import com.example.bank.bank_transactions.model.dto.TransactionDepositDto;
import com.example.bank.bank_transactions.model.dto.TransactionLoggerMessageDto;
import com.example.bank.bank_transactions.model.dto.TransactionTransferDto;
import com.example.bank.bank_transactions.model.enums.TransactionAction;
import com.example.bank.bank_transactions.model.enums.TransactionStatus;
import com.example.bank.bank_transactions.model.enums.TransactionType;
import com.example.bank.bank_transactions.repository.TransactionRepository;
import com.example.bank.bank_transactions.service.TransactionsService;
import org.springframework.transaction.annotation.Propagation;
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void createTransaction(TransactionTransferDto transactionData) {
        TransactionEntity decreaseTransaction = new TransactionEntity();
        decreaseTransaction.setTransactionType(TransactionType.TRANSFER);
        decreaseTransaction.setTransactionAction(TransactionAction.DECREASE);
        decreaseTransaction.setAmount(transactionData.getAmount());
        decreaseTransaction.setAccount(transactionData.getSourceAccount());

        TransactionEntity increaseTransaction = new TransactionEntity();
        increaseTransaction.setTransactionType(TransactionType.TRANSFER);
        increaseTransaction.setTransactionAction(TransactionAction.INCREASE);
        increaseTransaction.setAmount(transactionData.getAmount());
        increaseTransaction.setAccount(transactionData.getDestinationAccount());

        TransactionLoggerMessageDto decreaseLogMessage = new TransactionLoggerMessageDto(transactionRepository.save(decreaseTransaction), transactionData.getSourceAccount().getId(), TransactionStatus.CREATED);
        transactionsService.notifyObservers(decreaseLogMessage);

        TransactionLoggerMessageDto increaseLogMessage = new TransactionLoggerMessageDto(transactionRepository.save(increaseTransaction), transactionData.getDestinationAccount().getId(), TransactionStatus.CREATED);
        transactionsService.notifyObservers(increaseLogMessage);
    }

}



