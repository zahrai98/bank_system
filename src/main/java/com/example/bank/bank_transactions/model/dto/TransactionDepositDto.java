package com.example.bank.bank_transactions.model.dto;

import com.example.bank.user.model.BankAccountEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDepositDto {
    @NotNull
    private BigDecimal amount;
    private BankAccountEntity destinationAccount;
}
