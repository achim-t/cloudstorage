package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping
    public String postNote(Authentication authentication, Note note) {
        User user = userService.getUser(authentication.getName());
        if (note.getNoteid() > 0) {
            noteService.updateNote(note);
        } else {
            noteService.addNote(note, user.getUserId());
        }
        return "redirect:/home";
    }

    @GetMapping("/delete")
    public String deleteNote(@RequestParam("id") Integer noteid) {
        noteService.delete(noteid);
        return "redirect:/home";
    }
}
