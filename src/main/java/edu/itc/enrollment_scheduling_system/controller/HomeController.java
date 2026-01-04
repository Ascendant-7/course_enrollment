package edu.itc.enrollment_scheduling_system.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Authentication authentication) {
        // If user is already logged in, redirect to their appropriate dashboard
        if (authentication != null && authentication.isAuthenticated() 
            && !authentication.getName().equals("anonymousUser")) {
            
            boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            boolean isTeacher = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER"));
            boolean isStudent = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT"));
            
            if (isAdmin) return "redirect:/admin/dashboard";
            if (isTeacher) return "redirect:/teacher/dashboard";
            if (isStudent) return "redirect:/student/dashboard";
        }
        
        // Show home page for guests
        return "index";
    }
}