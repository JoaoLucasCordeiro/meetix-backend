package com.meetix.meetix_api.service;

import com.meetix.meetix_api.domain.user.User;
import com.meetix.meetix_api.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Carrega os dados do usuário pelo email (usado como username).

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.debug("Carregando dados do usuário para email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("Usuário não encontrado para email: {}", email);
                    return new UsernameNotFoundException("Usuário não encontrado: " + email);
                });

        logger.debug("Usuário encontrado: {} {}", user.getFirstName(), user.getLastName());

        // Cria um UserDetails com as informações do usuário
        // Por enquanto, todos os usuários têm as mesmas permissões (sem roles específicas)
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(new ArrayList<>()) // Sem authorities específicas por enquanto
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}