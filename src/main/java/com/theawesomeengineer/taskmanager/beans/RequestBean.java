package com.theawesomeengineer.taskmanager.beans;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@RequestScope
@Component
public class RequestBean {
    private double random;
    public RequestBean() {
        this.random = Math.random();
    }
    public void print() {
        System.out.println(this.random);
    }
}