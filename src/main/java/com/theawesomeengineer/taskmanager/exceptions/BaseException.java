package com.theawesomeengineer.taskmanager.exceptions;

import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private HttpStatusCode code;

    public BaseException(String message, HttpStatusCode code) {
        super(message);
        this.code = code;
    }
}
