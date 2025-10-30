package com.meetix.meetix_api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data // Anotação do Lombok que cria todos os getters e setters para nós.
public class JwtProperties {

    private String secret = "bWVldGl4LXNlY3JldC1rZXktZm9yLWp3dC10b2tlbi1zaWduaW5nLW1lZXRpeC1hcGktdjEtMjAyNQ==";

    //Define o tempo de vida de um token de acesso padrão, em milissegundos.
    //Padrão: 24 horas.

    private long expiration = 86400000L; // 24 * 60 * 60 * 1000

    private long refreshExpiration = 604800000L; // 7 * 24 * 60 * 60 * 1000

    private String tokenPrefix = "Bearer ";

    private String headerName = "Authorization";

}