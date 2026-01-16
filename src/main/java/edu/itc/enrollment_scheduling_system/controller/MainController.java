package edu.itc.enrollment_scheduling_system.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.itc.enrollment_scheduling_system.config.AccountDetails;

@Controller
@RequestMapping("/")
public class MainController {

    @GetMapping
    public String index(
        @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        // If user is not authenticated, show index page
        if (accountDetails == null) {
            return "shared/index";
        }

        // Route authenticated users to their role-specific dashboard
        if (accountDetails.getAuthorities()
            .stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))
        ) {
            return "redirect:/admin/dashboard";
        } else if (accountDetails.getAuthorities()
            .stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER"))
        ) {
            return "redirect:/teacher/dashboard";
        } else if (accountDetails.getAuthorities()
            .stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT"))
        ) {
            return "redirect:/student/dashboard";
        }
        
        // Default fallback for authenticated users without a specific role
        return "shared/index";
    }
}
