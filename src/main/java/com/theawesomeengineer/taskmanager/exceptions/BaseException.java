package com.theawesomeengineer.taskmanager.exceptions;

import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private HttpStatusCode code;
    private String detailMessage;

    public BaseException(String message, String detailMessage, HttpStatusCode code) {
        super(message);
        this.detailMessage = detailMessage;
        this.code = code;
    }

    public BaseException(String message, HttpStatusCode code) {
        this(message, "", code);
    }
}
