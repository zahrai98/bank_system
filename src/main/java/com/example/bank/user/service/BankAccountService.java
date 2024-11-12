package com.example.bank.user.service;


import com.example.bank.bank_transactions.TransactionDepositStrategy;
import com.example.bank.bank_transactions.TransactionTransferStrategy;
import com.example.bank.bank_transactions.TransactionWithdrawStrategy;
import com.example.bank.bank_transactions.model.dto.*;
import com.example.bank.bank_transactions.model.enums.TransactionAction;
import com.example.bank.bank_transactions.model.enums.TransactionStatus;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

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
    private PlatformTransactionManager transactionManager;


    public List<BankAccountOut> getByUserId(Long userId) {
        return bankAccountRepository.getByUserId(userId).stream().map(BankAccountOut::new).collect(Collectors.toList());

    }

    @Transactional(rollbackFor = Exception.class)
    public BankAccountOut create(Long userId, BankAccountIn bankAccountIn) throws ExecutionException, InterruptedException {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() ->
                new SystemException(HttpStatus.NOT_FOUND, "user not found", 404));
        Callable<BankAccountOut> createCallable = () -> {
            BankAccountEntity bankAccountEntity = bankAccountIn.convertToEntity(null);
            bankAccountEntity.setUser(userEntity);
            return new BankAccountOut(bankAccountRepository.save(bankAccountEntity));
        };
        return executorCallerService.execute(createCallable).get();
    }

    //    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean depositBankAccount(TransactionDepositInDto transactionDepositInDto) throws ExecutionException, InterruptedException {
        Callable<Boolean> deposit = () -> {
            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
            return transactionTemplate.execute(status -> {
                try {
                    BankAccountEntity bankAccountEntity = bankAccountRepository.findAccountByIdWithLock(transactionDepositInDto.getDestinationAccountId());
                    if (bankAccountEntity == null){
                        throw new SystemException(HttpStatus.NOT_FOUND, "bank account not found", 404);
                    }
                    bankAccountEntity.setBalance(bankAccountEntity.getBalance().add(transactionDepositInDto.getAmount()));
                    bankAccountRepository.save(bankAccountEntity);
                    transactionService.makeTransaction(transactionDepositStrategy, new TransactionDepositDto(transactionDepositInDto.getAmount(), bankAccountEntity));

                    return true;
                } catch (Exception e) {
                    status.setRollbackOnly();
                    TransactionLoggerMessageDto logMessage = new TransactionLoggerMessageDto(transactionDepositInDto.getDestinationAccountId(),
                            TransactionAction.INCREASE, TransactionType.DEPOSIT, transactionDepositInDto.getAmount(),
                            LocalDateTime.now(), TransactionStatus.FAILED);
                    transactionService.notifyObservers(logMessage);
                    return false;
//                    throw new SystemException(HttpStatus.NOT_ACCEPTABLE, "Transaction failed, rolling back.", 406);
                }
            });
        };
        return executorCallerService.execute(deposit).get();
    }

    public boolean withdrawBankAccount(TransactionWithdrawInDto transactionWithdrawInDto) throws
            ExecutionException, InterruptedException {
        Callable<Boolean> withdraw = () -> {
            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
            return transactionTemplate.execute(status -> {
                try {
                    BankAccountEntity bankAccountEntity = bankAccountRepository.findAccountByIdWithLock(transactionWithdrawInDto.getSourceAccountId());
                    if (bankAccountEntity == null){
                        throw new SystemException(HttpStatus.NOT_FOUND, "bank account not found", 404);
                    }
                    if (bankAccountEntity.getBalance().compareTo(transactionWithdrawInDto.getAmount()) >= 0) {
                        bankAccountEntity.setBalance(bankAccountEntity.getBalance().subtract(transactionWithdrawInDto.getAmount()));
                    } else {throw new SystemException(HttpStatus.BAD_REQUEST, "insufficient balance", 400);}
                    bankAccountRepository.save(bankAccountEntity);
                    transactionService.makeTransaction(transactionWithdrawStrategy, new TransactionWithdrawDto(transactionWithdrawInDto.getAmount(), bankAccountEntity));

                    return true;
                } catch (Exception e) {
                    status.setRollbackOnly();
                    TransactionLoggerMessageDto logMessage = new TransactionLoggerMessageDto(transactionWithdrawInDto.getSourceAccountId(),
                            TransactionAction.DECREASE, TransactionType.WITHDRAW, transactionWithdrawInDto.getAmount(),
                            LocalDateTime.now(), TransactionStatus.FAILED);
                    transactionService.notifyObservers(logMessage);
                    return false;
                }
            });
        };
        return executorCallerService.execute(withdraw).get();
    }

    public boolean transferBankAccount(TransactionTransferInDto transactionTransferInDto) throws ExecutionException, InterruptedException {
        Callable<Boolean> transfer = () -> {
            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
            return transactionTemplate.execute(status -> {
                try {
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

                    BigDecimal sourceAccountBalance = sourceBankAccount.getBalance();

                    if (sourceAccountBalance.compareTo(transactionTransferInDto.getAmount()) >= 0) {
                        sourceBankAccount.setBalance(sourceBankAccount.getBalance().subtract(transactionTransferInDto.getAmount()));
                        bankAccountRepository.save(sourceBankAccount);
                        destinationBankAccount.setBalance(destinationBankAccount.getBalance().add(transactionTransferInDto.getAmount()));
                        bankAccountRepository.save(destinationBankAccount);
                        transactionService.makeTransaction(transactionTransferStrategy,
                                new TransactionTransferDto(transactionTransferInDto.getAmount(), sourceBankAccount, destinationBankAccount));
                    } else {
                        throw new SystemException(HttpStatus.BAD_REQUEST, "insufficient balance", 400);
                    }
                    return Boolean.TRUE;
                } catch (Exception e) {
                    status.setRollbackOnly();
                    TransactionLoggerMessageDto increaseTransaction = new TransactionLoggerMessageDto(transactionTransferInDto.getDestinationAccountId(),
                            TransactionAction.INCREASE, TransactionType.TRANSFER, transactionTransferInDto.getAmount(),
                            LocalDateTime.now(), TransactionStatus.FAILED);
                    TransactionLoggerMessageDto decreaseTransaction = new TransactionLoggerMessageDto(transactionTransferInDto.getSourceAccountId(),
                            TransactionAction.DECREASE, TransactionType.TRANSFER, transactionTransferInDto.getAmount(),
                            LocalDateTime.now(), TransactionStatus.FAILED);
                    transactionService.notifyObservers(increaseTransaction);
                    transactionService.notifyObservers(decreaseTransaction);
                    throw new SystemException(HttpStatus.NOT_ACCEPTABLE, "Transaction failed, rolling back.", 406);
                }
            });
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