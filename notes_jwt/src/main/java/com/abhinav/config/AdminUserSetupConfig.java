package com.abhinav.config;

import com.abhinav.entity.User;
import com.abhinav.repository.UserRepository;
import com.abhinav.service.JwtService;
import com.abhinav.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Optional;

@Configuration
public class AdminUserSetupConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @PostConstruct
    public void setupAdminUser() {
        String adminUsername = "admin";
        String adminPassword = "adminPassword";

        // Check if admin user already exists
        Optional<User> existingAdminUser = userRepository.findByUsername(adminUsername);

        if (existingAdminUser.isEmpty()) {
            // If not, create the admin user
            User adminUser = new User();
            adminUser.setUsername(adminUsername);
            adminUser.setPassword(adminPassword);
//            adminUser.setRoles(Collections.singletonList("ADMIN"));  // Set the admin role
            adminUser.setAdmin(true);  // Set the isAdmin flag to true
            String token=jwtService.generateToken(adminUsername);
            System.out.println("CHECK ADMIN ACCESS TOKEN IN CONSOLE");

            System.out.println("--------------------------------------");

            System.out.println();


            System.out.println(token);

            System.out.println();


            // Save the admin user
            userRepository.save(adminUser);

            System.out.println("Admin user created successfully.");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
