package com.meetix.meetix_api.exception;

public class JwtAuthenticationException extends RuntimeException {

    // Construtor com mensagem de erro.

    public JwtAuthenticationException(String message) {
        super(message);
    }

    // Construtor com mensagem de erro e causa.

    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    // Construtor apenas com a causa.

    public JwtAuthenticationException(Throwable cause) {
        super(cause);
    }
}