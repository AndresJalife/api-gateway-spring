package com.jalife.apigatewayjava.exception;


/**
 * Exception thrown while updating a password and the validation doesnt match.
 */

public class PasswordsDontMatchException extends Exception {
    public PasswordsDontMatchException() {
        super("Passwords don't match");
    }
}

