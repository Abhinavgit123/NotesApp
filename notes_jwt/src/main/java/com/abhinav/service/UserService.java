package com.abhinav.service;

import com.abhinav.entity.User;
import com.abhinav.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Couldn't find user by username " + username));
    }

    public boolean existsAdminUser() {
        return userRepository.existsByAdmin(true);
    }

    public User createUser(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }


        // Create a new user
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password)); // Password is hashed before storing

        return userRepository.save(newUser);
    }

    public void makeUserAdmin(String currentUsername, String targetUsername) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + currentUsername));

        if (user!=null && user.isAdmin()) {
            User targetUser = userRepository.findByUsername(targetUsername)
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + targetUsername));

            if (targetUser != null) {
                targetUser.setAdmin(true);

                userRepository.save(targetUser);
            }
        }
    }


}