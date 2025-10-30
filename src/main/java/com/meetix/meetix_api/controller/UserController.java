package com.meetix.meetix_api.controller;

import com.meetix.meetix_api.domain.user.User;
import com.meetix.meetix_api.domain.user.UserRequestDTO;
import com.meetix.meetix_api.domain.user.UserUpdateDTO;
import com.meetix.meetix_api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * Verifica se o usuário autenticado é o dono da conta que está sendo manipulada.
     * 
     * @param userId ID do usuário que está sendo manipulado
     * @return true se é o dono da conta, false caso contrário
     */
    private boolean isAccountOwner(UUID userId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return false;
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String authenticatedEmail = userDetails.getUsername();
            
            Optional<User> targetUser = userService.getUserById(userId);
            if (targetUser.isPresent()) {
                return targetUser.get().getEmail().equals(authenticatedEmail);
            }
            
            return false;
        } catch (Exception e) {
            logger.error("Erro ao verificar propriedade da conta: {}", e.getMessage());
            return false;
        }
    }

    // Obtem o email do usuário autenticado.

    private String getAuthenticatedUserEmail() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                return userDetails.getUsername();
            }
            return null;
        } catch (Exception e) {
            logger.error("Erro ao obter email do usuário autenticado: {}", e.getMessage());
            return null;
        }
    }
     // Criar usuário (endpoint administrativo).

    @PostMapping
    @PreAuthorize("isAuthenticated()") // Temporário: usuários autenticados podem criar
    public ResponseEntity<?> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            User created = userService.createUser(userRequestDTO);
            logger.info("Usuário criado via endpoint administrativo: {}", created.getEmail());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Usuário criado com sucesso",
                    "data", created
            ));
        } catch (IllegalArgumentException e) {
            logger.warn("Erro na criação de usuário: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage(),
                    "data", null
            ));
        }
    }


     // Endpoint para o usuário obter suas próprias informações.
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyProfile() {
        try {
            String authenticatedEmail = getAuthenticatedUserEmail();
            if (authenticatedEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "success", false,
                        "message", "Usuário não autenticado",
                        "data", null
                ));
            }

            Optional<User> user = userService.getUserByEmail(authenticatedEmail);
            if (user.isPresent()) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Perfil encontrado",
                        "data", user.get()
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Perfil não encontrado",
                        "data", null
                ));
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar perfil do usuário: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Erro interno do servidor",
                    "data", null
            ));
        }
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Usuários listados com sucesso",
                    "data", users
            ));
        } catch (Exception e) {
            logger.error("Erro ao listar usuários: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Erro interno do servidor",
                    "data", null
            ));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        try {
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Usuário encontrado",
                        "data", user.get()
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Usuário não encontrado",
                        "data", null
                ));
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar usuário por ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Erro interno do servidor",
                    "data", null
            ));
        }
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            Optional<User> user = userService.getUserByEmail(email);
            if (user.isPresent()) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Usuário encontrado",
                        "data", user.get()
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Usuário não encontrado",
                        "data", null
                ));
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar usuário por email: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Erro interno do servidor",
                    "data", null
            ));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody UserUpdateDTO userUpdateDTO) {
        try {
            // REGRA DE NEGÓCIO: Usuário só pode editar sua própria conta
            if (!isAccountOwner(id)) {
                logger.warn("Tentativa de editar conta de outro usuário. ID solicitado: {}, Usuário autenticado: {}", 
                           id, getAuthenticatedUserEmail());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                        "success", false,
                        "message", "Você só pode editar sua própria conta",
                        "data", null
                ));
            }

            Optional<User> updated = userService.updateUser(id, userUpdateDTO);
            if (updated.isPresent()) {
                logger.info("Usuário atualizou sua própria conta: {}", updated.get().getEmail());
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Sua conta foi atualizada com sucesso",
                        "data", updated.get()
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Conta não encontrada",
                        "data", null
                ));
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Erro na atualização da conta do usuário {}: {}", getAuthenticatedUserEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage(),
                    "data", null
            ));
        } catch (Exception e) {
            logger.error("Erro inesperado ao atualizar conta: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Erro interno do servidor",
                    "data", null
            ));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        try {
            // REGRA DE NEGÓCIO: Usuário só pode deletar sua própria conta
            if (!isAccountOwner(id)) {
                logger.warn("Tentativa de deletar conta de outro usuário. ID solicitado: {}, Usuário autenticado: {}", 
                           id, getAuthenticatedUserEmail());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                        "success", false,
                        "message", "Você só pode deletar sua própria conta",
                        "data", null
                ));
            }

            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                logger.info("Usuário deletou sua própria conta. ID: {}, Email: {}", id, getAuthenticatedUserEmail());
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Sua conta foi deletada com sucesso",
                        "data", null
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Conta não encontrada",
                        "data", null
                ));
            }
        } catch (RuntimeException e) {
            logger.error("Erro ao deletar conta do usuário {}: {}", getAuthenticatedUserEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Erro ao deletar conta: " + e.getMessage(),
                    "data", null
            ));
        } catch (Exception e) {
            logger.error("Erro inesperado ao deletar conta: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Erro interno do servidor",
                    "data", null
            ));
        }
    }

     // Endpoint para o usuário atualizar sua própria conta (sem precisar do ID).
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateMyProfile(@RequestBody UserUpdateDTO userUpdateDTO) {
        try {
            String authenticatedEmail = getAuthenticatedUserEmail();
            if (authenticatedEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "success", false,
                        "message", "Usuário não autenticado",
                        "data", null
                ));
            }

            Optional<User> currentUser = userService.getUserByEmail(authenticatedEmail);
            if (currentUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Perfil não encontrado",
                        "data", null
                ));
            }

            Optional<User> updated = userService.updateUser(currentUser.get().getId_user(), userUpdateDTO);
            if (updated.isPresent()) {
                logger.info("Usuário atualizou seu perfil: {}", updated.get().getEmail());
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Seu perfil foi atualizado com sucesso",
                        "data", updated.get()
                ));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                        "success", false,
                        "message", "Erro ao atualizar perfil",
                        "data", null
                ));
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Erro na atualização do perfil do usuário {}: {}", getAuthenticatedUserEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage(),
                    "data", null
            ));
        } catch (Exception e) {
            logger.error("Erro inesperado ao atualizar perfil: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Erro interno do servidor",
                    "data", null
            ));
        }
    }

      // Endpoint para o usuário deletar sua própria conta (sem precisar do ID).

    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteMyAccount() {
        try {
            String authenticatedEmail = getAuthenticatedUserEmail();
            if (authenticatedEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "success", false,
                        "message", "Usuário não autenticado",
                        "data", null
                ));
            }

            Optional<User> currentUser = userService.getUserByEmail(authenticatedEmail);
            if (currentUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Perfil não encontrado",
                        "data", null
                ));
            }

            boolean deleted = userService.deleteUser(currentUser.get().getId_user());
            if (deleted) {
                logger.info("Usuário deletou sua própria conta: {}", authenticatedEmail);
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Sua conta foi deletada com sucesso. Você será desconectado.",
                        "data", null
                ));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                        "success", false,
                        "message", "Erro ao deletar conta",
                        "data", null
                ));
            }
        } catch (RuntimeException e) {
            logger.error("Erro ao deletar conta do usuário {}: {}", getAuthenticatedUserEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Erro ao deletar conta: " + e.getMessage(),
                    "data", null
            ));
        } catch (Exception e) {
            logger.error("Erro inesperado ao deletar conta: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Erro interno do servidor",
                    "data", null
            ));
        }
    }
}
