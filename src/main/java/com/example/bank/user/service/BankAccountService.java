package com.example.bank.user.service;


import com.example.bank.bank_transactions.TransactionDepositStrategy;
import com.example.bank.bank_transactions.TransactionTransferStrategy;
import com.example.bank.bank_transactions.TransactionWithdrawStrategy;
import com.example.bank.bank_transactions.model.dto.*;
import com.example.bank.bank_transactions.model.enums.TransactionAction;
import com.example.bank.bank_transactions.model.enums.TransactionType;
import com.example.bank.bank_transactions.service.TransactionsService;
import com.example.bank.common.dto.PageableDto;
import com.example.bank.common.service.ExecutorCallerService;
import com.example.bank.config.exceptions.SystemException;
import com.example.bank.user.model.BankAccountEntity;
import com.example.bank.user.model.UserEntity;
import com.example.bank.user.model.dto.BankAccountIn;
import com.example.bank.user.model.dto.BankAccountOut;
import com.example.bank.user.repository.BankAccountRepository;
import com.example.bank.user.repository.UserRepository;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final TransactionsService transactionService;
    private final ExecutorCallerService executorCallerService;
    private final TransactionDepositStrategy transactionDepositStrategy;
    private final TransactionWithdrawStrategy transactionWithdrawStrategy;
    private final TransactionTransferStrategy transactionTransferStrategy;


    public BankAccountOut getByUserId(Long userId) {
        return new BankAccountOut(bankAccountRepository.getByUserId(userId));
    }

    @Transactional(rollbackFor = Exception.class)
    public BankAccountOut create(Long userId, BankAccountIn bankAccountIn) throws ExecutionException, InterruptedException {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() ->
                new SystemException(HttpStatus.NOT_FOUND, "user not found", 404));
        Callable<BankAccountOut> createCallable = () -> {
            BankAccountEntity bankAccountEntity = bankAccountIn.convertToEntity(null);
            bankAccountEntity.setUser(userEntity);
            BankAccountEntity savedBankAccount = bankAccountRepository.save(bankAccountEntity);
            return new BankAccountOut(bankAccountRepository.save(bankAccountEntity));
        };
        return executorCallerService.execute(createCallable).get();
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean depositBankAccount(TransactionDepositInDto transactionDepositInDto) throws ExecutionException, InterruptedException {
        Callable<Boolean> deposit = () -> {
            BankAccountEntity bankAccountEntity = bankAccountRepository.findAccountByIdWithLock(transactionDepositInDto.getDestinationAccountId());
            bankAccountEntity.setBalance(bankAccountEntity.getBalance() + transactionDepositInDto.getAmount());
            bankAccountRepository.save(bankAccountEntity);
            transactionService.makeTransaction(transactionDepositStrategy, new TransactionDepositDto(transactionDepositInDto.getAmount(), bankAccountEntity));
            return Boolean.TRUE;
        };
        return executorCallerService.execute(deposit).get();
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean withdrawBankAccount(TransactionWithdrawInDto transactionWithdrawInDto) throws ExecutionException, InterruptedException {
        Callable<Boolean> withdraw = () -> {
            BankAccountEntity bankAccountEntity = bankAccountRepository.findAccountByIdWithLock(transactionWithdrawInDto.getSourceAccountId());
            Integer accountBalance = bankAccountEntity.getBalance();
            if (accountBalance.compareTo(transactionWithdrawInDto.getAmount()) >= 0) {
                transactionService.makeTransaction(transactionWithdrawStrategy, new TransactionWithdrawDto(transactionWithdrawInDto.getAmount(), bankAccountEntity));
            } else {
                throw new SystemException(HttpStatus.BAD_REQUEST, "insufficient balance", 400);
            }
            return Boolean.TRUE;
        };
        return executorCallerService.execute(withdraw).get();
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean transferBankAccount(TransactionTransferInDto transactionTransferInDto) throws ExecutionException, InterruptedException {
        Callable<Boolean> transfer = () -> {
            List<BankAccountEntity> bankAccounts = bankAccountRepository.findTwoAccountByIdWithLock(
                    transactionTransferInDto.getSourceAccountId(), transactionTransferInDto.getDestinationAccountId());

            if (bankAccounts.size() != 2) {
                throw new SystemException(HttpStatus.NOT_FOUND, "accounts not found", 404);
            }
            BankAccountEntity sourceBankAccount = bankAccounts.get(1);
            BankAccountEntity destinationBankAccount = bankAccounts.get(0);

            Long accountId = bankAccounts.get(0).getId();
            if (accountId.equals(transactionTransferInDto.getSourceAccountId())) {
                sourceBankAccount = bankAccounts.get(0);
                destinationBankAccount = bankAccounts.get(1);
            } else {
                sourceBankAccount = bankAccounts.get(1);
                destinationBankAccount = bankAccounts.get(0);
            }

            Integer sourceAccountBalance = sourceBankAccount.getBalance();

            if (sourceAccountBalance.compareTo(transactionTransferInDto.getAmount()) >= 0) {
                transactionService.makeTransaction(transactionTransferStrategy,
                        new TransactionTransferDto(transactionTransferInDto.getAmount(), sourceBankAccount, destinationBankAccount));
            } else {
                throw new SystemException(HttpStatus.BAD_REQUEST, "insufficient balance", 400);
            }
            return Boolean.TRUE;
        };

        return executorCallerService.execute(transfer).get();
    }

    public List<BankAccountOut> getAll(PageableDto pageableDto) {
        Pageable pageable = PageRequest.of(pageableDto.getPage() - 1, pageableDto.getSize());
        return bankAccountRepository.findAll(pageable).stream().map(BankAccountOut::new).collect(Collectors.toList());
    }


    public List<TransactionOutDto> getTransactionReport(Long accountId, PageableDto pageableDto) {
        return transactionService.getAllByAccountId(pageableDto, accountId);
    }

}