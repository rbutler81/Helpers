package com.custom;

public class ArgNotFoundException extends RuntimeException {

    public ArgNotFoundException(String description){
        super("Argument not provided: " + "<" + description + ">");
    }
}
