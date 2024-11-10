package com.example.bank.user.controller;

import com.example.bank.common.dto.PageableDto;
import com.example.bank.user.model.dto.UserIn;
import com.example.bank.user.model.dto.UserOut;
import com.example.bank.user.service.UserService;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping(path = "")
    public ResponseEntity<UserOut> create(@Valid @RequestBody UserIn userIn) {
        return ResponseEntity.ok(userService.create(userIn));
    }

    @GetMapping(path = "")
    public ResponseEntity<List<UserOut>> getAll(
            @Valid PageableDto pageableDto
    ) {
        return ResponseEntity.ok(userService.getAll(pageableDto));
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<UserOut> getById(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(userService.getById(userId));
    }
}