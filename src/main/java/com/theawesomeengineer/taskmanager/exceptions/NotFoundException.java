package com.theawesomeengineer.taskmanager.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    public NotFoundException(String message, String detailMessage) {
        super(message, detailMessage, HttpStatus.NOT_FOUND);
    }

    public NotFoundException(String message) {
        this(message, "");
    }
}
