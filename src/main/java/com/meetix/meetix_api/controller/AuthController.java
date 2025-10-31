package com.meetix.meetix_api.controller;

import com.meetix.meetix_api.domain.auth.LoginRequestDTO;
import com.meetix.meetix_api.domain.auth.LoginResponseDTO;
import com.meetix.meetix_api.domain.user.UserRequestDTO;
import com.meetix.meetix_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para autenticação e registro de usuários")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login de usuário", description = "Autentica um usuário com email e senha")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário", description = "Cria uma nova conta de usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso",
                    content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    public ResponseEntity<LoginResponseDTO> register(@Valid @RequestBody UserRequestDTO userRequest) {
        LoginResponseDTO response = authService.register(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/validate")
    @Operation(summary = "Validar token JWT", description = "Verifica se um token JWT é válido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token válido"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado")
    })
    public ResponseEntity<LoginResponseDTO> validateToken(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        LoginResponseDTO response = authService.getUserFromToken(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Realiza logout do usuário (remove token no cliente)")
    public ResponseEntity<Map<String, String>> logout() {
        return ResponseEntity.ok(Map.of(
                "message", "Logout realizado com sucesso. Remova o token do lado cliente."
        ));
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Verifica se o serviço de autenticação está funcionando")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "OK",
                "message", "Serviço de autenticação está funcionando",
                "timestamp", System.currentTimeMillis()
        ));
    }
}
