package com.dypcoe.qsdta.exception.dao;

public class UserConnectionException extends Exception {
    public UserConnectionException(String Message, Throwable throwable) {
        super(Message, throwable);
    }
}
