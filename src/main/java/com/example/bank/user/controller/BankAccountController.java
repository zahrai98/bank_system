package com.example.bank.user.controller;


import com.example.bank.bank_transactions.model.dto.TransactionInDto;
import com.example.bank.bank_transactions.model.dto.TransactionOutDto;
import com.example.bank.bank_transactions.model.dto.TransferTransactionInDto;
import com.example.bank.common.dto.PageableDto;
import com.example.bank.user.model.dto.BankAccountIn;
import com.example.bank.user.model.dto.BankAccountOut;
import com.example.bank.user.service.BankAccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Tag(name = "Account Controller")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/account")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @GetMapping(path = "")
    public ResponseEntity<List<BankAccountOut>> getAll(@Valid PageableDto pageableDto) {
        return ResponseEntity.ok(bankAccountService.getAll(pageableDto));
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<BankAccountOut> getByUserId(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(bankAccountService.getByUserId(userId));
    }

    @PostMapping(path = "/{userId}")
    public ResponseEntity<BankAccountOut> create(@PathVariable(name = "userId") Long userId,
                                                 @Valid @RequestBody BankAccountIn bankAccountIn)
            throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(bankAccountService.create(userId, bankAccountIn));
    }

    @PutMapping("/{accountId}/deposit")
    public ResponseEntity<Boolean> deposit(@Valid @RequestBody TransactionInDto transactionInDto,
                                           @PathVariable(name = "accountId") Long accountId) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(bankAccountService.depositBankAccount(accountId, transactionInDto));
    }

    @PutMapping("/{accountId}/withdraw")
    public ResponseEntity<Boolean> withdraw(@Valid @RequestBody TransactionInDto transactionInDto,
                                            @PathVariable(name = "accountId") Long accountId) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(bankAccountService.withdrawBankAccount(accountId, transactionInDto));
    }

    @PutMapping("/transfer")
    public ResponseEntity<Boolean> transfer(@Valid @RequestBody TransferTransactionInDto transferTransactionInDto) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(bankAccountService.transferBankAccount(transferTransactionInDto));
    }

    @GetMapping(path = "/{accountId}/transactions")
    public ResponseEntity<List<TransactionOutDto>> getAccountTransactionReport(@PathVariable(name = "accountId") Long accountId,
                                                                            @Valid PageableDto pageableDto) {
        return ResponseEntity.ok(bankAccountService.getTransactionReport(accountId, pageableDto));
    }
}