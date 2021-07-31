package com.fabhotels.wallet.exception;

public class PasswordIncorrectException extends Exception {
    public PasswordIncorrectException (Integer userId) {
    	super("Password is incorrect for "+userId);
    }
    

}
