package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.dto.AdminUserUpdateForm;
import edu.itc.enrollment_scheduling_system.model.Role;
import edu.itc.enrollment_scheduling_system.model.User;
import edu.itc.enrollment_scheduling_system.repository.RoleRepository;
import edu.itc.enrollment_scheduling_system.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class UserManagementController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserManagementController(UserRepository userRepository,
                                   RoleRepository roleRepository,
                                   PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/management")
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user-management";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findById(id).orElse(null);
        
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/admin/users/management";
        }

        AdminUserUpdateForm form = new AdminUserUpdateForm();
        form.setId(user.getId());
        form.setUsername(user.getUsername());
        form.setEmail(user.getEmail());
        form.setFirstName(user.getFirstName());
        form.setLastName(user.getLastName());
        form.setPhone(user.getPhone());
        form.setAddress(user.getAddress());
        form.setBio(user.getBio());
        form.setEnabled(user.isEnabled());
        form.setApproved(user.isApproved());
        
        // Get first role
        if (!user.getRoles().isEmpty()) {
            form.setRoleId(user.getRoles().iterator().next().getId());
        }

        model.addAttribute("form", form);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("user", user);
        
        return "user-edit";
    }

    @PostMapping("/{id}/edit")
    public String updateUser(@PathVariable Long id,
                            @Valid @ModelAttribute("form") AdminUserUpdateForm form,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        User user = userRepository.findById(id).orElse(null);
        
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/admin/users/management";
        }

        // Validate username uniqueness (if changed)
        if (!user.getUsername().equals(form.getUsername())) {
            if (userRepository.findByUsername(form.getUsername()).isPresent()) {
                bindingResult.rejectValue("username", "error.username", "Username already exists");
            }
        }

        // Validate email uniqueness (if changed)
        if (!user.getEmail().equals(form.getEmail())) {
            if (userRepository.findByEmail(form.getEmail()).isPresent()) {
                bindingResult.rejectValue("email", "error.email", "Email already exists");
            }
        }

        // Validate password if provided
        if (form.getNewPassword() != null && !form.getNewPassword().trim().isEmpty()) {
            if (form.getNewPassword().length() < 6) {
                bindingResult.rejectValue("newPassword", "error.newPassword", 
                    "Password must be at least 6 characters");
            }
            if (!form.getNewPassword().equals(form.getConfirmPassword())) {
                bindingResult.rejectValue("confirmPassword", "error.confirmPassword", 
                    "Passwords do not match");
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("user", user);
            return "user-edit";
        }

        try {
            // Update user details
            user.setUsername(form.getUsername());
            user.setEmail(form.getEmail());
            user.setFirstName(form.getFirstName());
            user.setLastName(form.getLastName());
            user.setPhone(form.getPhone());
            user.setAddress(form.getAddress());
            user.setBio(form.getBio());
            user.setEnabled(form.getEnabled() != null ? form.getEnabled() : false);
            user.setApproved(form.getApproved() != null ? form.getApproved() : false);

            // Update password if provided
            if (form.getNewPassword() != null && !form.getNewPassword().trim().isEmpty()) {
                user.setPassword(passwordEncoder.encode(form.getNewPassword()));
            }

            // Update role
            Role newRole = roleRepository.findById(form.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid role selected"));
            
            user.getRoles().clear();
            user.getRoles().add(newRole);

            userRepository.save(user);

            redirectAttributes.addFlashAttribute("success", 
                "User '" + user.getUsername() + "' updated successfully!");
            return "redirect:/admin/users/management";

        } catch (Exception e) {
            model.addAttribute("error", "Error updating user: " + e.getMessage());
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("user", user);
            return "user-edit";
        }
    }

    @PostMapping("/{id}/toggle-status")
    public String toggleUserStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User user = userRepository.findById(id).orElse(null);
        
        if (user != null) {
            user.setEnabled(!user.isEnabled());
            userRepository.save(user);
            
            String status = user.isEnabled() ? "enabled" : "disabled";
            redirectAttributes.addFlashAttribute("success", 
                "User '" + user.getUsername() + "' has been " + status);
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found");
        }
        
        return "redirect:/admin/users/management";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User user = userRepository.findById(id).orElse(null);
        
        if (user != null) {
            String username = user.getUsername();
            userRepository.delete(user);
            redirectAttributes.addFlashAttribute("success", 
                "User '" + username + "' has been deleted");
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found");
        }
        
        return "redirect:/admin/users/management";
    }
}