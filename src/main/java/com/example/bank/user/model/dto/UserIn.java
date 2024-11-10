package com.example.bank.user.model.dto;

import com.example.bank.user.model.UserEntity;
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
public class UserIn {
    @NotBlank
    @NotNull
    private String firstName;
    @NotBlank
    @NotNull
    private String lastName;
    @NotBlank
    @NotNull
    private String nationalCode;

    public UserEntity convertToEntity(UserEntity userEntity) {
        if (userEntity == null) {
            userEntity = new UserEntity();
        }
        userEntity.setFirstName(this.firstName);
        userEntity.setLastName(this.lastName);
        userEntity.setNationalCode(this.nationalCode);
        return userEntity;
    }
}
