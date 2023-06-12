package com.jalife.apigatewayjava.exception;


/**
 * Exception thrown when a user is not found
 */
public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super("User not found");
    }
}
