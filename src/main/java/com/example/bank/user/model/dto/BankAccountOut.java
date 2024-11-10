package com.example.bank.user.model.dto;

import com.example.bank.user.model.BankAccountEntity;
import com.example.bank.user.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountOut {
    private Long id;
    private String accountNumber;
    private Integer balance;
    private UserOut user;

    public BankAccountOut(BankAccountEntity bankAccountEntity) {
        if (bankAccountEntity != null) {
            this.id = bankAccountEntity.getId();
            this.balance = bankAccountEntity.getBalance();
            this.accountNumber = bankAccountEntity.getAccountNumber();
            this.user = new UserOut(bankAccountEntity.getUser());
        }
    }


    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class UserOut {
        private Long userId;
        private String firstName;
        private String lastName;
        private String nationalCode;

        public UserOut(UserEntity userEntity) {
            if (userEntity != null) {
                this.userId = userEntity.getId();
                this.firstName = userEntity.getFirstName();
                this.lastName = userEntity.getLastName();
                this.nationalCode = userEntity.getNationalCode();
            }
        }
    }
}
