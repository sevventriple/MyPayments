package com.example.mypayments.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BillingDetailsNotFoundException extends RuntimeException {
    public BillingDetailsNotFoundException() {
        super("Account or card was not found");
    }
}
