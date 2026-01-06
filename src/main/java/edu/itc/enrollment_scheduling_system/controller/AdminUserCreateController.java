package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.dto.AdminCreateUserForm;
import edu.itc.enrollment_scheduling_system.model.Role;
import edu.itc.enrollment_scheduling_system.model.User;
import edu.itc.enrollment_scheduling_system.repository.RoleRepository;
import edu.itc.enrollment_scheduling_system.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.Objects;

@Controller
@RequestMapping("/admin/users")
public class AdminUserCreateController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserCreateController(UserRepository userRepository,
                                    RoleRepository roleRepository,
                                    PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("form", new AdminCreateUserForm());
        model.addAttribute("roles", roleRepository.findAll());
        return "admin-register";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("form") AdminCreateUserForm form,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        // Validate username uniqueness
        if (userRepository.findByUsername(form.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "error.username", "Username already exists");
        }

        // Validate email uniqueness
        if (userRepository.findByEmail(form.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "error.email", "Email already exists");
        }

        // Validate password confirmation
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", 
                "Passwords do not match");
        }

        // Validate role selection
        if (form.getRoleId() == null) {
            bindingResult.rejectValue("roleId", "error.roleId", "Please select a role");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleRepository.findAll());
            return "admin-register";
        }

        try {
            Long roleId = Objects.requireNonNull(form.getRoleId(), "roleId must not be null");
            // Get the selected role
            Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role selected"));

            // Create new user
            User user = new User();
            user.setUsername(form.getUsername());
            user.setEmail(form.getEmail());
            user.setFirstName(form.getFirstName());
            user.setLastName(form.getLastName());
            user.setPassword(passwordEncoder.encode(form.getPassword()));
            user.setApproved(true);  // Admin-created users are auto-approved
            user.setEnabled(true);
            user.getRoles().add(role);

            userRepository.save(user);

            redirectAttributes.addFlashAttribute("success", 
                "User '" + user.getUsername() + "' created successfully!");
            return "redirect:/admin/users/management";

        } catch (Exception e) {
            model.addAttribute("error", "Error creating user: " + e.getMessage());
            model.addAttribute("roles", roleRepository.findAll());
            return "admin-register";
        }
    }
}