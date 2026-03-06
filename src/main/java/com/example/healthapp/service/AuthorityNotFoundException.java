package com.example.healthapp.service;

public class AuthorityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AuthorityNotFoundException(String authorityName) {
        super("Authority not found in database: " + authorityName);
    }
}
