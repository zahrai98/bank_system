package com.example.bank.common.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@AllArgsConstructor
@Service
public class ExecutorCallerService {

    private ExecutorService executorService;

    public <T> Future<T> execute(Callable<T> callable) {
        return executorService.submit(callable);
    }

}