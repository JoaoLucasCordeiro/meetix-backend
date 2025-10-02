package com.meetix.meetix_api.service;

import com.meetix.meetix_api.domain.user.User;
import com.meetix.meetix_api.domain.user.UserRequestDTO;
import com.meetix.meetix_api.domain.user.UserResponseDTO;
import com.meetix.meetix_api.domain.user.UserUpdateDTO;
import com.meetix.meetix_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }
        
        User user = new User();
        user.setNome(userRequestDTO.getNome());
        user.setSobrenome(userRequestDTO.getSobrenome());
        user.setEmail(userRequestDTO.getEmail());
        user.setSenha(passwordEncoder.encode(userRequestDTO.getSenha()));
        user.setInstagram(userRequestDTO.getInstagram());
        user.setFaculdade(userRequestDTO.getFaculdade());
        user.setCurso(userRequestDTO.getCurso());
        
        User savedUser = userRepository.save(user);
        return new UserResponseDTO(savedUser);
    }
    
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserResponseDTO::new);
    }
    
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return new UserResponseDTO(user);
    }
    
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return new UserResponseDTO(user);
    }
    
    public UserResponseDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        if (userUpdateDTO.getEmail() != null && !userUpdateDTO.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userUpdateDTO.getEmail())) {
                throw new RuntimeException("Email já está em uso");
            }
            user.setEmail(userUpdateDTO.getEmail());
        }
        
        if (userUpdateDTO.getNome() != null) {
            user.setNome(userUpdateDTO.getNome());
        }
        
        if (userUpdateDTO.getSobrenome() != null) {
            user.setSobrenome(userUpdateDTO.getSobrenome());
        }
        
        if (userUpdateDTO.getSenha() != null && !userUpdateDTO.getSenha().isEmpty()) {
            user.setSenha(passwordEncoder.encode(userUpdateDTO.getSenha()));
        }
        
        if (userUpdateDTO.getInstagram() != null) {
            user.setInstagram(userUpdateDTO.getInstagram());
        }
        
        if (userUpdateDTO.getFaculdade() != null) {
            user.setFaculdade(userUpdateDTO.getFaculdade());
        }
        
        if (userUpdateDTO.getCurso() != null) {
            user.setCurso(userUpdateDTO.getCurso());
        }
        
        User updatedUser = userRepository.save(user);
        return new UserResponseDTO(updatedUser);
    }
    
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        userRepository.delete(user);
    }
    
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
