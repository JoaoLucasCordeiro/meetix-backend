package com.meetix.meetix_api.service;

import com.meetix.meetix_api.domain.auth.LoginRequestDTO;
import com.meetix.meetix_api.domain.auth.LoginResponseDTO;
import com.meetix.meetix_api.domain.user.User;
import com.meetix.meetix_api.domain.user.UserRequestDTO;
import com.meetix.meetix_api.exception.JwtAuthenticationException;
import com.meetix.meetix_api.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Serviço responsável pela autenticação e autorização de usuários.
 * 
 * Este serviço gerencia:
 * - Login de usuários com validação de credenciais
 * - Registro de novos usuários
 * - Geração de tokens JWT
 * - Validação de tokens existentes
 * 
 * @author Meetix Team
 * @version 1.0
 */
@Service
@Transactional
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(
            UserRepository userRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Realiza o login do usuário com email e senha.
     *
     * @param loginRequest Dados de login (email e senha)
     * @return Resposta com token JWT e dados do usuário
     * @throws JwtAuthenticationException se as credenciais forem inválidas
     */
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        logger.debug("Tentativa de login para email: {}", loginRequest.email());

        try {
            // Autentica o usuário usando Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );

            // Busca o usuário no banco de dados
            User user = userRepository.findByEmail(loginRequest.email())
                    .orElseThrow(() -> new JwtAuthenticationException("Usuário não encontrado"));

            // Gera o token JWT
            String token = jwtService.generateToken(
                    user.getId_user(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName()
            );

            logger.info("Login realizado com sucesso para usuário: {}", loginRequest.email());

            return new LoginResponseDTO(
                    token,
                    user.getId_user(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName()
            );

        } catch (AuthenticationException e) {
            logger.warn("Falha na autenticação para email: {} - {}", loginRequest.email(), e.getMessage());
            throw new JwtAuthenticationException("Credenciais inválidas");
        } catch (Exception e) {
            logger.error("Erro inesperado durante login para email: {} - {}", loginRequest.email(), e.getMessage());
            throw new JwtAuthenticationException("Erro interno durante autenticação");
        }
    }

    /**
     * Registra um novo usuário no sistema.
     *
     * @param userRequest Dados do usuário para registro
     * @return Resposta com token JWT e dados do usuário criado
     * @throws JwtAuthenticationException se o email já estiver em uso
     */
    public LoginResponseDTO register(UserRequestDTO userRequest) {
        logger.debug("Tentativa de registro para email: {}", userRequest.email());

        // Verifica se o email já está em uso
        if (userRepository.findByEmail(userRequest.email()).isPresent()) {
            logger.warn("Tentativa de registro com email já existente: {}", userRequest.email());
            throw new JwtAuthenticationException("Email já está em uso");
        }

        try {
            // Cria o novo usuário
            User newUser = new User();
            newUser.setFirstName(userRequest.firstName());
            newUser.setLastName(userRequest.lastName());
            newUser.setEmail(userRequest.email());
            newUser.setPassword(passwordEncoder.encode(userRequest.password()));
            newUser.setInstagram(userRequest.instagram());
            newUser.setUniversity(userRequest.university());
            newUser.setCourse(userRequest.course());
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setUpdatedAt(LocalDateTime.now());

            // Salva o usuário no banco
            User savedUser = userRepository.save(newUser);

            // Gera o token JWT
            String token = jwtService.generateToken(
                    savedUser.getId_user(),
                    savedUser.getEmail(),
                    savedUser.getFirstName(),
                    savedUser.getLastName()
            );

            logger.info("Usuário registrado com sucesso: {}", userRequest.email());

            return new LoginResponseDTO(
                    token,
                    savedUser.getId_user(),
                    savedUser.getEmail(),
                    savedUser.getFirstName(),
                    savedUser.getLastName()
            );

        } catch (Exception e) {
            logger.error("Erro durante registro do usuário: {} - {}", userRequest.email(), e.getMessage());
            throw new JwtAuthenticationException("Erro interno durante registro");
        }
    }

    /**
     * Busca um usuário pelo email.
     *
     * @param email Email do usuário
     * @return Usuário encontrado ou Optional vazio
     */
    @Transactional(readOnly = true)
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Valida se um token JWT é válido para um usuário específico.
     *
     * @param token Token JWT
     * @param email Email do usuário
     * @return true se o token é válido, false caso contrário
     */
    public boolean validateToken(String token, String email) {
        try {
            String tokenEmail = jwtService.extractEmail(token);
            return tokenEmail.equals(email) && !jwtService.isTokenExpired(token);
        } catch (Exception e) {
            logger.warn("Falha na validação do token para email: {} - {}", email, e.getMessage());
            return false;
        }
    }

    /**
     * Extrai informações do usuário a partir de um token JWT válido.
     *
     * @param token Token JWT
     * @return Dados do usuário extraídos do token
     * @throws JwtAuthenticationException se o token for inválido
     */
    public LoginResponseDTO getUserFromToken(String token) {
        try {
            return new LoginResponseDTO(
                    token,
                    jwtService.extractUserId(token),
                    jwtService.extractEmail(token),
                    jwtService.extractFirstName(token),
                    jwtService.extractLastName(token)
            );
        } catch (Exception e) {
            logger.error("Erro ao extrair dados do usuário do token: {}", e.getMessage());
            throw new JwtAuthenticationException("Token inválido");
        }
    }
}