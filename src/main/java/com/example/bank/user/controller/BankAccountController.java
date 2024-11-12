package com.example.bank.user.controller;


import com.example.bank.bank_transactions.model.dto.*;
import com.example.bank.common.dto.PageableDto;
import com.example.bank.user.model.dto.BankAccountIn;
import com.example.bank.user.model.dto.BankAccountOut;
import com.example.bank.user.service.BankAccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/account")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @GetMapping(path = "/")
    public ResponseEntity<List<BankAccountOut>> getAll(@Valid PageableDto pageableDto) {
        return ResponseEntity.ok(bankAccountService.getAll(pageableDto));
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<List<BankAccountOut>> getByUserId(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(bankAccountService.getByUserId(userId));
    }

    @PostMapping(path = "/{userId}")
    public ResponseEntity<BankAccountOut> create(@PathVariable(name = "userId") Long userId,
                                                 @Valid @RequestBody BankAccountIn bankAccountIn)
            throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(bankAccountService.create(userId, bankAccountIn));
    }

    @PutMapping("/deposit")
    public ResponseEntity<Boolean> deposit(@Valid @RequestBody TransactionDepositInDto transactionDepositInDto) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(bankAccountService.depositBankAccount(transactionDepositInDto));
    }

    @PutMapping("/withdraw")
    public ResponseEntity<Boolean> withdraw(@Valid @RequestBody TransactionWithdrawInDto transactionWithdrawInDto) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(bankAccountService.withdrawBankAccount(transactionWithdrawInDto));
    }

    @PutMapping("/transfer")
    public ResponseEntity<Boolean> transfer(@Valid @RequestBody TransactionTransferInDto transactionTransferInDto) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(bankAccountService.transferBankAccount(transactionTransferInDto));
    }

    @GetMapping(path = "/{accountId}/transactions")
    public ResponseEntity<List<TransactionOutDto>> getAccountTransactionReport(@PathVariable(name = "accountId") Long accountId,
                                                                            @Valid PageableDto pageableDto) {
        return ResponseEntity.ok(bankAccountService.getTransactionReport(accountId, pageableDto));
    }
}