package com.example.bank.bank_transactions.service;

import com.example.bank.bank_transactions.model.dto.TransactionLoggerMessageDto;
import com.example.bank.common.service.Observer;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;


@Service
public class TransactionLoggerService implements Observer {

    private BufferedWriter writer;

    @Override
    public void update(Object message) {
        if (message instanceof TransactionLoggerMessageDto transactionLoggerMessageDto) {
            try {
                if (writer != null) {
                    String logMessage = "Transaction log : transaction ID: ";
                    Long transactionId = transactionLoggerMessageDto.getTransactionId();
                    logMessage += Objects.requireNonNullElse(transactionId, "unknown");

                    writer.write(logMessage +
                            " , account ID: " +
                            transactionLoggerMessageDto.getAccountId() +
                            " , transaction type: " +
                            transactionLoggerMessageDto.getTransactionType().name() +
                            " , transaction action: " +
                            transactionLoggerMessageDto.getTransactionAction().name() +
                            " , transaction amount: " +
                            transactionLoggerMessageDto.getAmount() +
                            " , transaction status: " +
                            transactionLoggerMessageDto.getTransactionStatus().name()
                    );
                    writer.newLine();
                }
            } catch (IOException e) {
//                TODO
                System.out.println("Error while writing transaction log");
            }
        }
    }

    @PostConstruct
    public void init() {
        try {
            writer = new BufferedWriter(new FileWriter("log.txt", true));
        } catch (IOException e) {
//            TODO
            System.out.println("Error while writing transaction log");
        }
    }

    @PreDestroy
    public void cleanup() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
//            TODO
            System.out.println("Error while writing transaction log");
        }
    }
}