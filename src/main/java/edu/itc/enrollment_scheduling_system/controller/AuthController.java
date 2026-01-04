package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.dto.RegisterForm;
import edu.itc.enrollment_scheduling_system.model.User;
import edu.itc.enrollment_scheduling_system.repository.UserRepository;
import edu.itc.enrollment_scheduling_system.service.EnrollmentService;
import edu.itc.enrollment_scheduling_system.service.RegistrationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final RegistrationService registrationService;
    private final EnrollmentService enrollmentService;
    private final UserRepository userRepository;

    public AuthController(RegistrationService registrationService,
                         EnrollmentService enrollmentService,
                         UserRepository userRepository) {
        this.registrationService = registrationService;
        this.enrollmentService = enrollmentService;
        this.userRepository = userRepository;
    }

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
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterForm form,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        try {
            registrationService.registerUser(form);
            model.addAttribute("success", "Registration submitted! Please wait for admin approval.");
            model.addAttribute("registerForm", new RegisterForm());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "register";
    }

    @GetMapping("/my-courses")
    public String viewCourses(Model model,
                             @RequestParam(required = false) String role,
                             Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        boolean isTeacher = authentication.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER"));

        if (isTeacher) {
            model.addAttribute("courses", enrollmentService.getCoursesByTeacherId(user.getId()));
        } else {
            model.addAttribute("courses", enrollmentService.getCoursesByStudentId(user.getId()));
        }

        model.addAttribute("user", user);
        return "my-courses";
    }
}