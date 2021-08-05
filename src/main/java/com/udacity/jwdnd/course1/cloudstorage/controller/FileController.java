package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URI;

@Controller
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping
    public String postFile(Authentication authentication, MultipartFile fileUpload, RedirectAttributes redirectAttributes) {
        User user = userService.getUser(authentication.getName());

        try {
            if (fileUpload.getSize() == 0) throw new NullPointerException();
            fileService.addFile(fileUpload, user.getUserId());
            redirectAttributes.addFlashAttribute("success", "File created");
        } catch (IOException | NullPointerException e) {
            redirectAttributes.addFlashAttribute("error", "File upload failed");
        }

        redirectAttributes.addFlashAttribute("tab", "files");
        return "redirect:/home";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> getFile(Authentication authentication, @RequestParam("id") Integer fileId, RedirectAttributes redirectAttributes) {
        User user = userService.getUser(authentication.getName());
        File file = fileService.get(fileId, user.getUserId());

        if (file == null) {
            redirectAttributes.addFlashAttribute("error", "Something went wrong");
            redirectAttributes.addFlashAttribute("tab", "files");
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/home"))
                    .build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new ByteArrayResource(file.getFiledata()));
    }

    @GetMapping("/delete")
    public String deleteFile(@RequestParam("id") Integer fileid, Authentication authentication, RedirectAttributes redirectAttributes) {
        User user = userService.getUser(authentication.getName());
        if (fileService.delete(fileid, user.getUserId())) {
            redirectAttributes.addFlashAttribute("success", "File deleted");
        } else {
            redirectAttributes.addFlashAttribute("error", "Something went wrong");
        }
        redirectAttributes.addFlashAttribute("tab", "files");
        return "redirect:/home";
    }
}