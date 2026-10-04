package com.ewerton.blog_simples.exception;

public class NameAlreadyExistsException extends RuntimeException{
    public NameAlreadyExistsException(String message) { super(message);}
}
