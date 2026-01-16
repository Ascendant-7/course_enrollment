package edu.itc.enrollment_scheduling_system.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import edu.itc.enrollment_scheduling_system.security.AccountDetails;

@Controller
public class MainController {

    public String index(
        @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        if (
            accountDetails.getAuthorities()
            .stream()
            .anyMatch(auth -> auth.getAuthority()
            .equals("ROLE_ADMIN"))
        ) return "redirect:/admin/dashboard";
        else if (
            accountDetails.getAuthorities()
            .stream()
            .anyMatch(auth -> auth.getAuthority()
            .equals("ROLE_TEACHER"))
        ) return "redirect:/teacher/dashboard";
        else if (
            accountDetails.getAuthorities()
            .stream()
            .anyMatch(auth -> auth.getAuthority()
            .equals("ROLE_STUDENT"))
        ) return "redirect:/student/dashboard";
        
        return "index";
    }
}