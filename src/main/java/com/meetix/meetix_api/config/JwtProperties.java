package com.meetix.meetix_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configurações específicas para JWT.
 * 
 * Esta classe centraliza todas as propriedades relacionadas ao JWT,
 * permitindo fácil configuração através do application.properties.
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * Chave secreta para assinatura dos tokens.
     * Em produção, deve ser uma chave forte e única.
     */
    private String secret = "bWVldGl4LXNlY3JldC1rZXktZm9yLWp3dC10b2tlbi1zaWduaW5nLW1lZXRpeC1hcGktdjEtMjAyNQ==";

    /**
     * Tempo de expiração do token em milissegundos.
     * Valor padrão: 24 horas (86400000 ms).
     */
    private long expiration = 86400000L; // 24 horas

    /**
     * Tempo de expiração para refresh tokens em milissegundos.
     * Valor padrão: 7 dias (604800000 ms).
     */
    private long refreshExpiration = 604800000L; // 7 dias

    /**
     * Prefixo para o header Authorization.
     * Valor padrão: "Bearer ".
     */
    private String tokenPrefix = "Bearer ";

    /**
     * Nome do header que contém o token.
     * Valor padrão: "Authorization".
     */
    private String headerName = "Authorization";

    // Getters e Setters
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public long getRefreshExpiration() {
        return refreshExpiration;
    }

    public void setRefreshExpiration(long refreshExpiration) {
        this.refreshExpiration = refreshExpiration;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }
}