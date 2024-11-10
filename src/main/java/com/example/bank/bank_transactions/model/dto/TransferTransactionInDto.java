package com.example.bank.bank_transactions.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferTransactionInDto extends TransactionInDto {
    @NotBlank
    @NotNull
    private Long sourceAccountId;
    @NotBlank
    @NotNull
    private Long destinationAccountId;
}
