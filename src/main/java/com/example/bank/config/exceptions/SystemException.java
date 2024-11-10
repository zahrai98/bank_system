package com.example.bank.config.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class SystemException extends RuntimeException {
    HttpStatus status;
    Object message;
    int code;

    public SystemException(HttpStatus status, Object message, int code) {
        super(message != null ? message.toString() : null);
        this.status = status;
        this.message = message;
        this.code = code;
    }


    @Override
    public String getMessage() {
        return message != null ? message.toString() : null;
    }
}
