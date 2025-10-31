package com.meetix.meetix_api.controller;

import com.meetix.meetix_api.domain.user.UserRequestDTO;
import com.meetix.meetix_api.domain.user.UserResponseDTO;
import com.meetix.meetix_api.domain.user.UserUpdateDTO;
import com.meetix.meetix_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Gerenciamento de usuários")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Criar usuário", description = "Cria um novo usuário no sistema")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO data) {
        UserResponseDTO user = userService.createUser(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping
    @Operation(summary = "Listar usuários", description = "Retorna lista de todos os usuários")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar usuário por email", description = "Retorna um usuário pelo email")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        UserResponseDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza dados de um usuário")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateDTO data
    ) {
        UserResponseDTO user = userService.updateUser(id, data);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar usuário", description = "Remove um usuário do sistema")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
