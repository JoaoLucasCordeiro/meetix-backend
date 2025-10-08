package com.meetix.meetix_api.controller;

import com.meetix.meetix_api.domain.auth.LoginRequestDTO;
import com.meetix.meetix_api.domain.auth.LoginResponseDTO;
import com.meetix.meetix_api.domain.user.UserRequestDTO;
import com.meetix.meetix_api.exception.JwtAuthenticationException;
import com.meetix.meetix_api.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller responsável pelos endpoints de autenticação.
 * 
 * Este controller fornece:
 * - Endpoint de login (/auth/login)
 * - Endpoint de registro (/auth/register)
 * - Endpoint de validação de token (/auth/validate)
 * - Tratamento centralizado de exceções de autenticação
 * 
 * Todos os endpoints retornam respostas padronizadas JSON.
 * 
 * @author Meetix Team
 * @version 1.0
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint para login de usuário.
     * 
     * @param loginRequest Dados de login (email e senha)
     * @return ResponseEntity com token JWT e dados do usuário ou erro
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        logger.info("Recebida requisição de login para email: {}", loginRequest.email());

        try {
            LoginResponseDTO response = authService.login(loginRequest);
            
            logger.info("Login realizado com sucesso para usuário: {}", loginRequest.email());
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Login realizado com sucesso",
                    "data", response
            ));

        } catch (JwtAuthenticationException e) {
            logger.warn("Falha no login para email: {} - {}", loginRequest.email(), e.getMessage());
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", e.getMessage(),
                    "data", null
            ));

        } catch (Exception e) {
            logger.error("Erro inesperado durante login: {}", e.getMessage(), e);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Erro interno do servidor",
                    "data", null
            ));
        }
    }

    /**
     * Endpoint para registro de novo usuário.
     * 
     * @param userRequest Dados do usuário para registro
     * @return ResponseEntity com token JWT e dados do usuário criado ou erro
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequestDTO userRequest) {
        logger.info("Recebida requisição de registro para email: {}", userRequest.email());

        try {
            LoginResponseDTO response = authService.register(userRequest);
            
            logger.info("Usuário registrado com sucesso: {}", userRequest.email());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Usuário registrado com sucesso",
                    "data", response
            ));

        } catch (JwtAuthenticationException e) {
            logger.warn("Falha no registro para email: {} - {}", userRequest.email(), e.getMessage());
            
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "success", false,
                    "message", e.getMessage(),
                    "data", null
            ));

        } catch (Exception e) {
            logger.error("Erro inesperado durante registro: {}", e.getMessage(), e);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Erro interno do servidor",
                    "data", null
            ));
        }
    }

    /**
     * Endpoint para validação de token JWT.
     * 
     * @param token Token JWT no header Authorization
     * @return ResponseEntity com dados do usuário se token válido ou erro
     */
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        logger.debug("Recebida requisição de validação de token");

        try {
            // Remove o prefixo "Bearer " se presente
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            LoginResponseDTO response = authService.getUserFromToken(token);
            
            logger.debug("Token validado com sucesso para usuário: {}", response.email());
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Token válido",
                    "data", response
            ));

        } catch (JwtAuthenticationException e) {
            logger.warn("Token inválido: {}", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "Token inválido",
                    "data", null
            ));

        } catch (Exception e) {
            logger.error("Erro inesperado durante validação de token: {}", e.getMessage(), e);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Erro interno do servidor",
                    "data", null
            ));
        }
    }

    /**
     * Endpoint para logout (opcional - apenas remove token do lado cliente).
     * 
     * @return ResponseEntity com mensagem de sucesso
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        logger.info("Recebida requisição de logout");
        
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Logout realizado com sucesso. Remova o token do lado cliente.",
                "data", null
        ));
    }

    /**
     * Endpoint de health check para verificar se o serviço de autenticação está funcionando.
     * 
     * @return ResponseEntity com status do serviço
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Serviço de autenticação está funcionando",
                "timestamp", System.currentTimeMillis()
        ));
    }

    /**
     * Tratamento global de exceções para este controller.
     * 
     * @param e Exceção capturada
     * @return ResponseEntity com mensagem de erro padronizada
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception e) {
        logger.error("Exceção não tratada no AuthController: {}", e.getMessage(), e);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor",
                "data", null
        ));
    }
}