package com.pao.escaperoom.exception;

public class EmailTakenException extends Exception{
    public EmailTakenException(String username){
        super("This email already exists.");
    }
}
