package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final NoteService noteService;
    private final UserService userService;

    public HomeController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping
    public String getHome(Model model, Principal principal) {
        User user = userService.getUser(principal.getName());

        model.addAttribute("notes", this.noteService.getNotes(user));
        return "home";
    }

    @PostMapping
    public String postHome(Model model, Authentication authentication, NoteForm noteForm) {
        noteForm.setUsername(authentication.getName());
        this.noteService.addNote(noteForm);
        noteForm.setText("");
        noteForm.setTitle("");
        model.addAttribute("notes", this.noteService.getNotes(userService.getUser(authentication.getName())));
        return "home";
    }
}
