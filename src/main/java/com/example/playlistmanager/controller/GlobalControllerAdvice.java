package com.example.playlistmanager.controller;

import com.example.playlistmanager.model.AppUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("currentUser")
    public AppUser currentUser(HttpSession session) {
        Object user = session.getAttribute("currentUser");
        return user instanceof AppUser appUser ? appUser : null;
    }
}
