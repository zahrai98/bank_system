package com.example.bank.user.model.dto;

import com.example.bank.user.model.BankAccountEntity;
import com.example.bank.user.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserOut {
    private Long id;
    private String firstName;
    private String lastName;
    private String nationalCode;
    private List<BankAccountOut> accounts;

    public UserOut(UserEntity userEntity) {
        if (userEntity != null) {
            this.id = userEntity.getId();
            this.firstName = userEntity.getFirstName();
            this.lastName = userEntity.getLastName();
            this.nationalCode = userEntity.getNationalCode();
            Set<BankAccountEntity> allAccounts = userEntity.getAccounts();
            if (allAccounts != null) {
                this.accounts = allAccounts.stream().map(BankAccountOut::new).collect(Collectors.toList());
            }
        }
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class BankAccountOut {
        private Long accountId;
        private String accountNumber;
        private Integer balance;

        public BankAccountOut(BankAccountEntity bankAccountEntity) {
            if (bankAccountEntity != null) {
                this.accountId = bankAccountEntity.getId();
                this.accountNumber = bankAccountEntity.getAccountNumber();
                this.balance = bankAccountEntity.getBalance();
            }
        }
    }
}
