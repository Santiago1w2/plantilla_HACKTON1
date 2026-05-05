package org.test.week06lab01.exeception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}