package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.dto.RegistrationDTO;
import edu.itc.enrollment_scheduling_system.service.RegistrationService;
import edu.itc.enrollment_scheduling_system.util.CustomStringUtil;

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

    // REMOVE OR COMMENT OUT THIS METHOD - it conflicts with HomeController
    /*
    @GetMapping("/")
    public String home() {
        return "index";
    }
    */

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute(
            "fields",
            CustomStringUtil.getRecordFieldNames(RegistrationDTO.class)
        );
        return "register";
    }

    @PostMapping("/register")
    public String register(
        @Valid @ModelAttribute RegistrationDTO form,
        BindingResult result,
        Model model,
        RedirectAttributes redirection
    ) {
        if (result.hasErrors()) {
            model.addAttribute(
                "fields",
                CustomStringUtil.getRecordFieldNames(RegistrationDTO.class)
            );
            return "register";
        }
        registrationService.registerUser(form);
        redirection.addAttribute(
            "successMessage",
            "account registered! please wait for admin to approve!"
        );
        return "redirect:/waiting-room";
    }
}