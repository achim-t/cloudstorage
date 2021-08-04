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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String postNote(Authentication authentication, Note note, RedirectAttributes redirectAttributes) {
        User user = userService.getUser(authentication.getName());
        if (note.getNoteid() > 0) {
            if (noteService.updateNote(note, user.getUserId())) {
                redirectAttributes.addFlashAttribute("success", "Note updated");
            } else {
                redirectAttributes.addFlashAttribute("error", "Something went wrong");
            }
        } else {
            noteService.addNote(note, user.getUserId());
            redirectAttributes.addFlashAttribute("success", "Note created");
        }
        redirectAttributes.addFlashAttribute("tab", "notes");
        return "redirect:/home";
    }

    @GetMapping("/delete")
    public String deleteNote(@RequestParam("id") Integer noteid, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (noteService.delete(noteid, userService.getUser(authentication.getName()).getUserId())) {
            redirectAttributes.addFlashAttribute("success", "Note deleted");
        } else {
            redirectAttributes.addFlashAttribute("error", "Something went wrong");
        }
        redirectAttributes.addFlashAttribute("tab", "notes");
        return "redirect:/home";
    }
}
