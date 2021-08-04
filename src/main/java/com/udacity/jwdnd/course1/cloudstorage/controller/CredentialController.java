package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/credentials")
public class CredentialController {
    private final CredentialService credentialService;
    private final UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping
    public String postCredential(Authentication authentication, Credential credential, RedirectAttributes redirectAttributes) {
        User user = userService.getUser(authentication.getName());
        if (credential.getCredentialid() > 0) {
            if (credentialService.updateCredential(credential, user.getUserId())) {
                redirectAttributes.addFlashAttribute("success", "Credential updated");
            } else {
                redirectAttributes.addFlashAttribute("error", "Something went wrong");
            }
        } else {
            credentialService.addCredential(credential, user.getUserId());
            redirectAttributes.addFlashAttribute("success", "Credential created");
        }
        redirectAttributes.addFlashAttribute("tab", "credentials");
        return "redirect:/home";
    }

    @GetMapping("/delete")
    public String deleteCredential(@RequestParam("id") Integer credentialid, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (credentialService.delete(credentialid, userService.getUser(authentication.getName()).getUserId())) {
            redirectAttributes.addFlashAttribute("success", "Credential deleted");
        } else {
            redirectAttributes.addFlashAttribute("error", "Something went wrong");
        }
        redirectAttributes.addFlashAttribute("tab", "credentials");
        return "redirect:/home";
    }
}