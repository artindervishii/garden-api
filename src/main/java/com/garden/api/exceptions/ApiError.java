package com.garden.api.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiError {

    private int status;
    private String message;
    private String path;
    private String timestamp;

    public ApiError(int status, String path, String message, String timestamp) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.timestamp = timestamp;
    }

}
