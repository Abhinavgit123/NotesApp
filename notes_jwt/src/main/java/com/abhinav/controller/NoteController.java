package com.abhinav.controller;

import com.abhinav.config.CustomUserDetails;
import com.abhinav.entity.Note;
import com.abhinav.entity.User;
import com.abhinav.repository.NoteRepository;
import com.abhinav.service.NoteService;
import com.abhinav.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/notes")
public class NoteController {
    @Autowired
    private  NoteService noteService;
    @Autowired
    private  UserService userService;
    @Autowired
    private NoteRepository noteRepository;

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping
    public ResponseEntity<?> getAllNotes() {
        try{
        String username = getCurrentUsername();
        User user=userService.findByUsername(username);
            List<Note> notes = noteService.getAllNotesForUser(user);
            return ResponseEntity.ok(notes);
        }catch (Exception e){
            return new ResponseEntity<>("User not authorized",HttpStatus.FORBIDDEN);
        }
    }


//    @GetMapping("/{noteId}")
//    public ResponseEntity<?> getNoteById(@PathVariable String noteId) {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        if (principal instanceof CustomUserDetails) {
//            CustomUserDetails customUserDetails = (CustomUserDetails) principal;
//            String username = customUserDetails.getUsername();
//            String jwtToken = customUserDetails.getJwtToken();
//
//            try {
//                User authenticatedUser = userService.findByUsername(username);
//                Note note = noteService.getNoteById(noteId, authenticatedUser);
//
//                if (note != null) {
//                    return ResponseEntity.ok(note);
//                } else {
//                    return ResponseEntity.notFound().build();
//                }
//            } catch (RuntimeException e) {
//                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }

    @GetMapping("/{noteId}")
    public ResponseEntity<?> getNoteById(@PathVariable String noteId) {
            String username = getCurrentUsername();
        try {
                User authenticatedUser = userService.findByUsername(username);
                Note note = noteService.getNoteById(noteId, authenticatedUser);

                if (note != null) {
                    return ResponseEntity.ok(note);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (RuntimeException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
        }

    @PostMapping
    public ResponseEntity<?> createNote(@RequestBody Note newNote) {
        String username = getCurrentUsername();
        Note savedNote = noteService.createNote(newNote, username);
        return new ResponseEntity<>(savedNote,HttpStatus.OK);
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<Note> updateNote(@PathVariable String noteId, @RequestBody Note updatedNote) {
        String username = getCurrentUsername();
        Note updatedNotes = noteService.updateNote(noteId, updatedNote, username);
        return ResponseEntity.ok(updatedNotes);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<?> deleteNote(@PathVariable String noteId) {
        try {
            String username = getCurrentUsername();
            noteService.deleteNoteById(noteId, username);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/{noteId}/share")
    public ResponseEntity<?> shareNote(@PathVariable String noteId, @RequestParam String sharedWithUsername) {
        try {
            String currentUsername = getCurrentUsername();
            User shareWithUser = userService.findByUsername(sharedWithUsername);
            Note sharedNote = noteService.shareNoteWithUser(noteId, shareWithUser, currentUsername);
            return ResponseEntity.ok(sharedNote);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/makeAdmin")
    public ResponseEntity<String> makeUserAdmin(@RequestParam String targetUsername) {
        try {
            String currentUsername = getCurrentUsername();
            userService.makeUserAdmin(currentUsername, targetUsername);
            return ResponseEntity.ok("User " + targetUsername + " is now an admin.");
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);
        }
    }
}