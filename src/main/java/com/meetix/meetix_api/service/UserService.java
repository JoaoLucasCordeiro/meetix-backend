package com.meetix.meetix_api.service;

import com.meetix.meetix_api.domain.user.User;
import com.meetix.meetix_api.domain.user.UserRequestDTO;
import com.meetix.meetix_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(UserRequestDTO data) {
        validateUserData(data);

        if (userRepository.existsByEmail(data.email())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User newUser = mapDtoToEntity(data);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(newUser);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> updateUser(UUID id, UserRequestDTO data) {
        validateUserData(data);

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User userToUpdate = optionalUser.get();

            if (!userToUpdate.getEmail().equals(data.email()) && userRepository.existsByEmail(data.email())) {
                throw new IllegalArgumentException("Email already registered");
            }

            userToUpdate.setFirstName(data.firstName());
            userToUpdate.setLastName(data.lastName());
            userToUpdate.setEmail(data.email());

            if (data.password() != null && !data.password().isBlank()) {
                userToUpdate.setPassword(data.password());
            }

            userToUpdate.setInstagram(data.instagram());
            userToUpdate.setUniversity(data.university());
            userToUpdate.setCourse(data.course());
            userToUpdate.setUpdatedAt(LocalDateTime.now());

            return Optional.of(userRepository.save(userToUpdate));
        } else {
            return Optional.empty();
        }
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    private void validateUserData(UserRequestDTO data) {
        if (data.firstName() == null || data.firstName().isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (data.lastName() == null || data.lastName().isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (data.email() == null || data.email().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!isValidEmail(data.email())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (data.password() == null || data.password().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (data.password().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private User mapDtoToEntity(UserRequestDTO data) {
        User user = new User();
        user.setFirstName(data.firstName());
        user.setLastName(data.lastName());
        user.setEmail(data.email());
        user.setPassword(data.password());
        user.setInstagram(data.instagram());
        user.setUniversity(data.university());
        user.setCourse(data.course());
        return user;
    }
}
