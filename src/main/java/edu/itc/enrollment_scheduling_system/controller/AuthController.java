package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.dto.RegistrationDTO;
import edu.itc.enrollment_scheduling_system.service.RegistrationService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registerForm", new RegistrationDTO("", "", "", "", ""));
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
        @Valid @ModelAttribute("registerForm") RegistrationDTO form,
        BindingResult result,
        Model model,
        RedirectAttributes redirection
    ) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        
        registrationService.registerUser(form);
        redirection.addFlashAttribute(
            "success",
            "Account registered! Please wait for admin approval before logging in."
        );
        return "redirect:/login";
    }
}
