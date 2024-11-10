package com.example.bank.bank_transactions.service;


import com.example.bank.bank_transactions.model.TransactionEntity;
import com.example.bank.bank_transactions.model.dto.TransactionLoggerMessageDto;
import com.example.bank.bank_transactions.model.dto.TransactionOutDto;
import com.example.bank.bank_transactions.model.enums.TransactionAction;
import com.example.bank.bank_transactions.model.enums.TransactionType;
import com.example.bank.bank_transactions.repository.TransactionRepository;
import com.example.bank.common.dto.PageableDto;
import com.example.bank.common.service.ExecutorCallerService;
import com.example.bank.common.service.Observer;
import com.example.bank.common.service.Subject;
import com.example.bank.user.model.BankAccountEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class TransactionsService extends Subject {

    private final TransactionRepository transactionRepository;
    private final ExecutorCallerService executorCallerService;
//
//    @Transactional(rollbackFor = Exception.class)
//    public void makeTransaction(BankAccountEntity account,
//                                TransactionIn model,
//                                TransactionMode mode,
//                                TransactionType type) {
//        Callable<Boolean> transactionCallable = () -> {
//            TransactionEntity transaction = new TransactionEntity();
//            transaction.setAccountId(account.getId());
//            transaction.setTransactionMode(mode);
//            transaction.setAmount(model.getAmount());
//            transaction.setCreated(LocalDateTime.now());
//            transaction.setTitle("for account no: " + account.getAccountNumber() + " at: " + transaction.getCreated());
//            dao.save(transaction);
//            dao.flush();
//
//            TransactionMessageDto message = new TransactionMessageDto(
//                    mode,
//                    type,
//                    transaction.getAmount(),
//                    transaction.getTitle(),
//                    transaction.getCreated()
//            );
//
//            notifyObservers(message);
//
//            return true;
//        };
//
//        executorCallerService.execute(transactionCallable);
//    }

    @Transactional(rollbackFor = Exception.class)
    public void makeTransaction(BankAccountEntity account,
                                Integer amount,
                                TransactionAction transactionAction,
                                TransactionType transactionType) {
        Callable<Boolean> transactionCallable = () -> {
            TransactionEntity transaction = new TransactionEntity();
            transaction.setTransactionAction(transactionAction);
            transaction.setAmount(amount);
            transaction.setAccount(account);
            transaction.setCreatedAt(LocalDateTime.now());
            transactionRepository.save(transaction);

            TransactionLoggerMessageDto logMessage = new TransactionLoggerMessageDto(transaction);
            notifyObservers(logMessage);
            return true;
        };

        executorCallerService.execute(transactionCallable);
    }

    public List<TransactionOutDto> getAllByAccountId(PageableDto pageableDto, Long accountId) {
        Pageable pageable = PageRequest.of(pageableDto.getPage() - 1, pageableDto.getSize());
        return transactionRepository.getAllByAccountId(accountId, pageable).stream().map(TransactionOutDto::new).collect(Collectors.toList());
    }

    @Override
    protected void notifyObservers(Object transaction) {
        for (Observer observer : super.getObservers()) {
            observer.update(transaction);
        }
    }
}