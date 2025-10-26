package com.esmee.poppick_backend.exception;

public class MovieListNotFoundException extends RuntimeException {
    public MovieListNotFoundException(String message) {
        super(message);
    }
}
