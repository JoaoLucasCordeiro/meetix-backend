package com.meetix.meetix_api.domain.user;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    
    private String nome;
    private String sobrenome;
    
    @Email(message = "Email deve ser válido")
    private String email;
    
    private String senha;
    private String instagram;
    private String faculdade;
    private String curso;
}