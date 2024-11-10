package com.example.bank.config;


import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;

@Configuration
public class ExecutorServiceConfig {
    private ExecutorService executorService;

    @Bean
    public ExecutorService executorService() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("user account Thread - ");
        executor.initialize();
        this.executorService = executor.getThreadPoolExecutor();
        return this.executorService;
    }

    @PreDestroy
    public void onApplicationEvent() {
        if (this.executorService != null) {
            this.executorService.shutdown();
        }
    }
}