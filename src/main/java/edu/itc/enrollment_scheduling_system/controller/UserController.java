package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.dto.UserProfileForm;
import edu.itc.enrollment_scheduling_system.model.User;
import edu.itc.enrollment_scheduling_system.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return "redirect:/login";

        UserProfileForm form = new UserProfileForm();
        form.setUsername(user.getUsername());
        form.setFullName(user.getFullName());
        form.setEmail(user.getEmail());
        form.setBio(user.getBio());
        form.setPhone(user.getPhone());
        form.setAddress(user.getAddress());

        model.addAttribute("user", user);
        model.addAttribute("form", form);
        return "user-profile";
    }

    @PostMapping("/profile")
    public String updateProfile(Authentication authentication,
                                @Valid @ModelAttribute("form") UserProfileForm form,
                                BindingResult bindingResult,
                                Model model) {
        String currentUsername = authentication.getName();
        User user = userRepository.findByUsername(currentUsername).orElse(null);
        if (user == null) return "redirect:/login";

        // Enforce non-editable full name in server side by overriding
        form.setFullName(user.getFullName());

        // If password change requested, validate
        boolean changingPassword = form.getNewPassword() != null && !form.getNewPassword().isBlank();
        if (changingPassword) {
            if (form.getOldPassword() == null || form.getOldPassword().isBlank()) {
                bindingResult.rejectValue("oldPassword", "oldPassword.required", "Old password is required");
            } else if (!passwordEncoder.matches(form.getOldPassword(), user.getPassword())) {
                bindingResult.rejectValue("oldPassword", "oldPassword.invalid", "Old password is incorrect");
            }
            if (form.getConfirmNewPassword() == null || !form.getConfirmNewPassword().equals(form.getNewPassword())) {
                bindingResult.rejectValue("confirmNewPassword", "password.mismatch", "Passwords do not match");
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "user-profile";
        }

        // Update editable fields
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        user.setBio(form.getBio());
        user.setPhone(form.getPhone());
        user.setAddress(form.getAddress());
        if (changingPassword) {
            user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        }
        userRepository.save(user);

        // Refresh model
        model.addAttribute("user", user);
        model.addAttribute("form", form);
        model.addAttribute("success", "Profile updated successfully");
        return "user-profile";
    }
}
