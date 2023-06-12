package com.jalife.apigatewayjava.exception;

/**
 * Exception thrown when a user already exists
 */
public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException() {
        super("User already exists");
    }
}
