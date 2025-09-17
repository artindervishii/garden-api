package com.garden.api.exceptions;

public class ResourceValidationException extends RuntimeException{
    public ResourceValidationException(String message){
        super(message);
    }

}
