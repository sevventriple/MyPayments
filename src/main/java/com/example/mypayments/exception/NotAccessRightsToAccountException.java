package com.example.mypayments.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotAccessRightsToAccountException extends RuntimeException {
    public NotAccessRightsToAccountException() {
        super("attempt to gain access to an account without access rights to it");
    }
}
