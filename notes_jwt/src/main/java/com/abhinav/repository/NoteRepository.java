package com.abhinav.repository;

import com.abhinav.entity.Note;
import com.abhinav.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    List<Note> findAllByUsersUsername(String username);

    List<Note> findAllByUsersContaining(User user);

    Optional<Note> findByIdAndUsersUsername(String noteId, String username);

    void deleteByIdAndUsersUsername(String noteId, String username);
}
