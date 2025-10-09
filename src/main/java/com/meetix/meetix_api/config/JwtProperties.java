package com.meetix.meetix_api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Mapeia e centraliza as configurações de JWT a partir do application.properties.
 * Utiliza o prefixo "jwt" para vincular as propriedades. Ex: jwt.secret=sua_chave
 */
@Component
@ConfigurationProperties(prefix = "jwt")
@Data // Anotação do Lombok que cria todos os getters e setters para nós.
public class JwtProperties {

    /**
     * Chave secreta usada para assinar e validar os tokens JWT.
     * IMPORTANTE: Este valor padrão é apenas para desenvolvimento.
     * Em produção, ele DEVE ser substituído por uma chave forte e segura no application.properties.
     */
    private String secret = "bWVldGl4LXNlY3JldC1rZXktZm9yLWp3dC10b2tlbi1zaWduaW5nLW1lZXRpeC1hcGktdjEtMjAyNQ==";

    /**
     * Define o tempo de vida de um token de acesso padrão, em milissegundos.
     * Padrão: 24 horas.
     */
    private long expiration = 86400000L; // 24 * 60 * 60 * 1000

    /**
     * Define o tempo de vida de um "refresh token", em milissegundos.
     * Usado para obter um novo token de acesso sem precisar fazer login novamente.
     * Padrão: 7 dias.
     */
    private long refreshExpiration = 604800000L; // 7 * 24 * 60 * 60 * 1000

    /**
     * Prefixo padrão que antecede o token no header HTTP.
     * Padrão: "Bearer ".
     */
    private String tokenPrefix = "Bearer ";

    /**
     * Nome do header HTTP onde o token JWT é esperado.
     * Padrão: "Authorization".
     */
    private String headerName = "Authorization";

    // Não precisamos mais dos getters e setters manuais.
    // O @Data cuida disso para nós!
}