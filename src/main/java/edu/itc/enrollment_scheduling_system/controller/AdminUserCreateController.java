package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.dto.AdminCreateUserForm;
import edu.itc.enrollment_scheduling_system.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/register")
public class AdminUserCreateController {

    private final RegistrationService registrationService;

    public AdminUserCreateController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public String form(Model model) {
        model.addAttribute("form", new AdminCreateUserForm());
        return "admin-register";
    }

    @PostMapping
    public String submit(@Valid @ModelAttribute("form") AdminCreateUserForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) return "admin-register";

        try {
            registrationService.registerAdmin(form);
            redirectAttributes.addFlashAttribute("message",
                "User " + form.getUsername() + " created and approved.");
            return "redirect:/admin/users";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "admin-register";
        }
    }
}