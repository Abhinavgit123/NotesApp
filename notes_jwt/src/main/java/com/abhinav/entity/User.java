package com.abhinav.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@RequiredArgsConstructor
@Document(collection = "user")
public class User {

    @Id
    private String id;
    @Indexed(unique = true)
    private String username;

    private String password;

    private boolean isAdmin;

    private Set<String> roles = new HashSet<>();

    public User(String username) {
        this.username = username;
    }
}
