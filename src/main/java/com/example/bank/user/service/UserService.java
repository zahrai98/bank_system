package com.example.bank.user.service;

import com.example.bank.common.dto.PageableDto;
import com.example.bank.config.exceptions.SystemException;
import com.example.bank.user.model.UserEntity;
import com.example.bank.user.model.dto.UserIn;
import com.example.bank.user.model.dto.UserOut;
import com.example.bank.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserOut create(UserIn userIn) {
        if (userRepository.findByNationalCode(userIn.getNationalCode()).isPresent()) {
            throw new SystemException(HttpStatus.CONFLICT, "user already exist", 409);
        }
        UserEntity userEntity = userIn.convertToEntity(null);
        return new UserOut(userRepository.save(userEntity));
    }

    public UserOut getById(Long id) {
        return new UserOut(userRepository.findById(id).orElseThrow(() ->
                new SystemException(HttpStatus.NOT_FOUND, "user not found", 404)));
    }

    public List<UserOut> getAll(PageableDto pageableDto) {
        Pageable pageable = PageRequest.of(pageableDto.getPage() - 1, pageableDto.getSize());
        return userRepository.findAll(pageable).stream().map(UserOut::new).collect(Collectors.toList());
    }

}