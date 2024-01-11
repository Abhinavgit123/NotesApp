package com.abhinav.service;

import com.abhinav.config.CustomUserDetails;
import com.abhinav.entity.Note;
import com.abhinav.entity.User;
import com.abhinav.repository.NoteRepository;
import com.abhinav.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    @Autowired
    private  NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    private final MongoTemplate mongoTemplate;

    public NoteService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Note> getAllNotesForUser(User user) {
        if (user.isAdmin()) {
            return noteRepository.findAll();
        } else {
            List<Note> userNotes = noteRepository.findAllByUsersContaining(user);

            return userNotes;
        }
    }


    public Note getNoteById(String noteId, User user) {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> new RuntimeException("Note not found"));

        if (note != null && (isAdmin(user) || isNoteAccessibleByUser(note, user))) {
            return note;
        } else {
            throw new RuntimeException("Note not accessible by the user");
        }
    }

    private boolean isAdmin(User user) {
        return user.getRoles().stream().anyMatch(role -> role.equalsIgnoreCase("ADMIN"));

    }

    private boolean isNoteAccessibleByUser(Note note, User user) {
        return note.getUsers().stream().anyMatch(u -> u.getUsername().equals(user.getUsername()));
    }

    public Note createNote(Note newNote, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        newNote.getUsers().add(user);

        newNote.setOwner(user);

        return noteRepository.save(newNote);
    }


    public Note updateNote(String noteId, Note updatedNote, String username) {
        User user = mongoTemplate.findOne(new Query(Criteria.where("username").is(username)), User.class);

        assert user != null;

        Query query = new Query(Criteria
                .where("id").is(noteId)
                .and("users.$id").is(user.getId()));

        System.out.println("Update Query: " + query.toString());

        Update update = new Update();

        if (updatedNote.getTitle() != null) {
            update.set("title", updatedNote.getTitle());
        }

        if (updatedNote.getContent() != null) {
            update.set("content", updatedNote.getContent());
        }

        Note existingNote = mongoTemplate.findAndModify(query, update, Note.class);

        return existingNote;
    }

    public void deleteNoteById(String noteId, String username) {
        Query userQuery = new Query(Criteria.where("username").is(username));
        User user = mongoTemplate.findOne(userQuery, User.class);
        if (user != null) {
            Query noteQuery = new Query(Criteria.where("_id").is(noteId).and("owner.id").is(user.getId()));
            Note note = mongoTemplate.findOne(noteQuery, Note.class);

            if (note != null) {
                mongoTemplate.remove(note);
            } else {
                throw new RuntimeException("Note not found or user does not have permission");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }


    public Note shareNoteWithUser(String noteId, User shareWithUser, String currentUserUsername) {
        Query userQuery = new Query(Criteria.where("username").is(currentUserUsername));
        User user = mongoTemplate.findOne(userQuery, User.class);

        if (user != null) {
            Query noteQuery = new Query(Criteria.where("_id").is(noteId).and("owner.id").is(user.getId()));
            Note note = mongoTemplate.findOne(noteQuery, Note.class);

            if (note != null && isUserValid(shareWithUser)) {
                // Add the user to the 'users' set
                note.getUsers().add(shareWithUser);
                return mongoTemplate.save(note);
            } else {
                throw new RuntimeException("Note not found, not owned by the current user, or invalid user to share with");
            }
        } else {
            throw new RuntimeException("Current user not found");
        }
    }

    public boolean isUserValid(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        return existingUser.isPresent();
    }

}