package com.example.bank.bank_transactions.model.dto;

import com.example.bank.user.model.BankAccountEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionWithdrawDto {
    @NotNull
    private Integer amount;
    private BankAccountEntity sourceAccount;
}
