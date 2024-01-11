package com.abhinav.repository;

import com.abhinav.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByAdmin(boolean isAdmin);
    boolean existsByUsername(String username);
}
