package com.garden.api.exceptions;

public class NotPendingException extends RuntimeException {
    public NotPendingException(String message) {
        super(message);
    }

}
