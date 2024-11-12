package com.example.bank.user.model.dto;

import com.example.bank.user.model.BankAccountEntity;
import com.example.bank.user.model.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountIn {
    @NotBlank
    @NotNull
    private String accountNumber;

    public BankAccountEntity convertToEntity(BankAccountEntity bankAccountEntity) {
        if (bankAccountEntity == null) {
            bankAccountEntity = new BankAccountEntity();
        }
        bankAccountEntity.setAccountNumber(this.accountNumber);
        bankAccountEntity.setBalance(BigDecimal.valueOf(0));
        return bankAccountEntity;
    }
}
