package com.abhinav.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@RequiredArgsConstructor
@Document(collection = "notes")
public class Note {

    @Id
    private String id;
    @Indexed
    private String title;

    private String content;

    @DBRef
    private User owner;

    @DBRef
    private Set<User> users = new HashSet<>();

    public void addUser(User user) {

        this.users.add(user);
    }

}