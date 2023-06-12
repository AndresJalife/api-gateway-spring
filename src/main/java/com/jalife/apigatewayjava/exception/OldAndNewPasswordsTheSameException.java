package com.jalife.apigatewayjava.exception;


/**
 * Exception thrown when the old and new passwords are the same
 */
public class OldAndNewPasswordsTheSameException extends Exception {
    public OldAndNewPasswordsTheSameException() {
        super("Old and new passwords are the same");
    }
}
