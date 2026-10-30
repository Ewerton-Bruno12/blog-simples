package com.ewerton.blog_simples.exception;

public class TagInUseException extends RuntimeException {
    public TagInUseException(String message) {
        super(message);
    }
}
