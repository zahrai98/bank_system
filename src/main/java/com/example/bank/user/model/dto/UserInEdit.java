package com.example.bank.user.model.dto;

import com.example.bank.user.model.UserEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInEdit {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    public UserEntity convertToEntity(UserEntity userEntity) {
        if (userEntity == null) {
            userEntity = new UserEntity();
        }
        userEntity.setFirstName(this.firstName);
        userEntity.setLastName(this.lastName);
        return userEntity;
    }
}
