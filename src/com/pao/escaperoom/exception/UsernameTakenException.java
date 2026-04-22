package com.pao.escaperoom.exception;

public class UsernameTakenException extends Exception{
    public UsernameTakenException(String username){
        super("This username is already taken.");
    }
}
