package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.dto.AdminUserUpdateForm;
import edu.itc.enrollment_scheduling_system.model.User;
import edu.itc.enrollment_scheduling_system.repository.RoleRepository;
import edu.itc.enrollment_scheduling_system.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
public class UserManagementController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public UserManagementController(UserRepository userRepository,
                                    RoleRepository roleRepository,
                                    PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-management";
    }

    @PostMapping("/{id}/approve")
    public String approveUser(@PathVariable(required = true) Long id, RedirectAttributes redirectAttributes) {
        User user = userRepository.findById(java.util.Objects.requireNonNull(id))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        user.setApproved(true);
        userRepository.save(user);
        
        redirectAttributes.addFlashAttribute("message", "User " + user.getUsername() + " approved successfully");
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/reject")
    public String rejectUser(@PathVariable(required = true) Long id, RedirectAttributes redirectAttributes) {
        User user = userRepository.findById(java.util.Objects.requireNonNull(id))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        user.setApproved(false);
        userRepository.save(user);
        
        redirectAttributes.addFlashAttribute("message", "User " + user.getUsername() + " approval revoked");
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable(required = true) Long id, RedirectAttributes redirectAttributes) {
        User user = userRepository.findById(java.util.Objects.requireNonNull(id))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        String username = user.getUsername();
        userRepository.delete(user);
        
        redirectAttributes.addFlashAttribute("message", "User " + username + " deleted successfully");
        return "redirect:/admin/users";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable(required = true) Long id, Model model) {
        User user = userRepository.findById(java.util.Objects.requireNonNull(id))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        AdminUserUpdateForm form = new AdminUserUpdateForm();
        form.setUsername(user.getUsername());
        // take the first role name or default
        String roleName = user.getRoles().stream().findFirst()
                .map(r -> r.getName().replace("ROLE_", ""))
                .orElse("STUDENT");
        form.setRole(roleName);
        form.setApproved(user.isApproved());

        model.addAttribute("form", form);
        model.addAttribute("userId", id);
        return "user-edit";
    }

    @PostMapping("/{id}/edit")
    public String editSubmit(@PathVariable(required = true) Long id,
                             @ModelAttribute("form") AdminUserUpdateForm form,
                             RedirectAttributes redirectAttributes) {

        User user = userRepository.findById(java.util.Objects.requireNonNull(id))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // unique username check (if changed)
        var other = userRepository.findByUsername(form.getUsername());
        if (other.isPresent() && !other.get().getId().equals(id)) {
            redirectAttributes.addFlashAttribute("message", "Username already taken");
            return "redirect:/admin/users/" + id + "/edit";
        }

        user.setUsername(form.getUsername());
        user.setApproved(form.isApproved());

        // set single role
        String roleName = "ROLE_" + form.getRole().toUpperCase();
        var role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
        user.getRoles().clear();
        user.getRoles().add(role);

        // optional password change
        if (form.getNewPassword() != null && !form.getNewPassword().isBlank()) {
            if (form.getNewPassword().length() < 6) {
                redirectAttributes.addFlashAttribute("message", "New password must be at least 6 characters");
                return "redirect:/admin/users/" + id + "/edit";
            }
            user.setPassword(encoder.encode(form.getNewPassword()));
        }

        userRepository.save(user);
        redirectAttributes.addFlashAttribute("message", "User updated");
        return "redirect:/admin/users";
    }
}