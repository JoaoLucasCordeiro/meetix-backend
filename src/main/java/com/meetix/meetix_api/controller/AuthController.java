package com.meetix.meetix_api.controller;

import com.meetix.meetix_api.domain.auth.LoginRequestDTO;
import com.meetix.meetix_api.domain.auth.LoginResponseDTO;
import com.meetix.meetix_api.domain.user.UserRequestDTO;
import com.meetix.meetix_api.service.AuthService;
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
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@Valid @RequestBody UserRequestDTO userRequest) {
        LoginResponseDTO response = authService.register(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<LoginResponseDTO> validateToken(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        LoginResponseDTO response = authService.getUserFromToken(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        return ResponseEntity.ok(Map.of(
                "message", "Logout realizado com sucesso. Remova o token do lado cliente."
        ));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "OK",
                "message", "Serviço de autenticação está funcionando",
                "timestamp", System.currentTimeMillis()
        ));
    }
}
