package org.test.week06lab01.exeception;
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}