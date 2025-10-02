package com.meetix.meetix_api.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    
    private Long id;
    private String nome;
    private String sobrenome;
    private String email;
    private String instagram;
    private String faculdade;
    private String curso;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.nome = user.getNome();
        this.sobrenome = user.getSobrenome();
        this.email = user.getEmail();
        this.instagram = user.getInstagram();
        this.faculdade = user.getFaculdade();
        this.curso = user.getCurso();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}