package com.meetix.meetix_api.exception;

/**
 * Exceção personalizada para erros relacionados ao JWT.
 * 
 * Esta classe permite um tratamento mais específico de erros
 * relacionados à autenticação e autorização via JWT.
 */
public class JwtAuthenticationException extends RuntimeException {

    /**
     * Construtor com mensagem de erro.
     *
     * @param message Mensagem descritiva do erro
     */
    public JwtAuthenticationException(String message) {
        super(message);
    }

    /**
     * Construtor com mensagem de erro e causa.
     *
     * @param message Mensagem descritiva do erro
     * @param cause Causa raiz da exceção
     */
    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Construtor apenas com a causa.
     *
     * @param cause Causa raiz da exceção
     */
    public JwtAuthenticationException(Throwable cause) {
        super(cause);
    }
}