package com.example.bank.bank_transactions.service;


import com.example.bank.bank_transactions.TransactionStrategy;
import com.example.bank.bank_transactions.model.dto.TransactionOutDto;
import com.example.bank.bank_transactions.repository.TransactionRepository;
import com.example.bank.common.dto.PageableDto;
import com.example.bank.common.service.ExecutorCallerService;
import com.example.bank.common.service.Observer;
import com.example.bank.common.service.Subject;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class TransactionsService extends Subject {

    private final TransactionRepository transactionRepository;
    private final ExecutorCallerService executorCallerService;

    @Transactional(rollbackFor = Exception.class)
    public <T> void makeTransaction(TransactionStrategy<T> strategy, T transactionDto) {
        Callable<Boolean> transactionCallable = strategy.createTransaction(transactionDto);
        executorCallerService.execute(transactionCallable);
    }

    public List<TransactionOutDto> getAllByAccountId(PageableDto pageableDto, Long accountId) {
        Pageable pageable = PageRequest.of(pageableDto.getPage() - 1, pageableDto.getSize());
        return transactionRepository.getAllByAccountId(accountId, pageable).stream().map(TransactionOutDto::new).collect(Collectors.toList());
    }

    @Override
    public void notifyObservers(Object transaction) {
        for (Observer observer : super.getObservers()) {
            observer.update(transaction);
        }
    }
}