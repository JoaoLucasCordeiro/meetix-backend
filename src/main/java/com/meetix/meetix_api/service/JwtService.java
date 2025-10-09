package com.meetix.meetix_api.service;

import com.meetix.meetix_api.config.JwtProperties;
import com.meetix.meetix_api.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private final JwtProperties jwtProperties;

    @Autowired
    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    // Gera um token JWT para o usuário especificado.

    public String generateToken(UUID userId, String email, String firstName, String lastName) {
        logger.debug("Gerando token JWT para usuário: {}", email);
        
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("firstName", firstName);
        extraClaims.put("lastName", lastName);
        extraClaims.put("userId", userId.toString());
        
        String token = generateToken(extraClaims, email);
        logger.info("Token JWT gerado com sucesso para usuário: {}", email);
        
        return token;
    }

    // Gera um token JWT com claims personalizados.

    public String generateToken(Map<String, Object> extraClaims, String email) {
        return buildToken(extraClaims, email, jwtProperties.getExpiration());
    }

    // Constrói o token JWT com todas as configurações necessárias.

    private String buildToken(Map<String, Object> extraClaims, String email, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(extraClaims)
                .subject(email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    // Extrai o email (subject) do token JWT.

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrai o ID do usuário do token JWT.

    public UUID extractUserId(String token) {
        String userIdString = extractClaim(token, claims -> claims.get("userId", String.class));
        return UUID.fromString(userIdString);
    }

    // Extrai o primeiro nome do usuário do token JWT.

    public String extractFirstName(String token) {
        return extractClaim(token, claims -> claims.get("firstName", String.class));
    }

    // Extrai o último nome do usuário do token JWT.

    public String extractLastName(String token) {
        return extractClaim(token, claims -> claims.get("lastName", String.class));
    }

    // Extrai a data de expiração do token JWT.

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extrai um claim específico do token JWT usando uma função de resolução.

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Valida se o token JWT é válido para o usuário especificado.

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String email = extractEmail(token);
            boolean isValid = email.equals(userDetails.getUsername()) && !isTokenExpired(token);
            
            if (isValid) {
                logger.debug("Token JWT válido para usuário: {}", email);
            } else {
                logger.warn("Token JWT inválido para usuário: {}", userDetails.getUsername());
            }
            
            return isValid;
        } catch (Exception e) {
            logger.error("Erro ao validar token JWT: {}", e.getMessage());
            return false;
        }
    }

    // Valida se um token JWT é válido para um usuário específico (sobrecarga com email).

    public boolean isTokenValid(String token, String email) {
        try {
            final String tokenEmail = extractEmail(token);
            return (tokenEmail.equals(email)) && !isTokenExpired(token);
        } catch (Exception e) {
            logger.warn("Erro ao validar token para email {}: {}", email, e.getMessage());
            return false;
        }
    }

    // Verifica se o token JWT expirou.

    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    // Extrai todos os claims do token JWT.

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            logger.warn("Token JWT expirado: {}", e.getMessage());
            throw new JwtAuthenticationException("Token JWT expirado", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT não suportado: {}", e.getMessage());
            throw new JwtAuthenticationException("Token JWT não suportado", e);
        } catch (MalformedJwtException e) {
            logger.error("Token JWT malformado: {}", e.getMessage());
            throw new JwtAuthenticationException("Token JWT malformado", e);
        } catch (SecurityException e) {
            logger.error("Token JWT inválido: {}", e.getMessage());
            throw new JwtAuthenticationException("Token JWT inválido", e);
        } catch (Exception e) {
            logger.error("Erro ao processar token JWT: {}", e.getMessage());
            throw new JwtAuthenticationException("Erro ao processar token JWT", e);
        }
    }

    // Obtém a chave de assinatura a partir da chave secreta configurada.

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Valida se a estrutura do token está correta (sem verificar assinatura).

    public boolean isTokenStructureValid(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        
        // JWT deve ter 3 partes separadas por pontos
        String[] parts = token.split("\\.");
        return parts.length == 3;
    }

    // Obtém o tempo restante até a expiração do token em milissegundos.

    public long getTimeUntilExpiration(String token) {
        try {
            Date expiration = extractExpiration(token);
            long timeRemaining = expiration.getTime() - new Date().getTime();
            return Math.max(0, timeRemaining);
        } catch (Exception e) {
            return 0;
        }
    }

    // Cria um token de acesso rápido apenas com informações básicas.

    public String generateSimpleToken(String email) {
        return generateToken(new HashMap<>(), email);
    }
}