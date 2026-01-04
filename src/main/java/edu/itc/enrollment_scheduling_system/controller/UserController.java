package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.dto.UserProfileForm;
import edu.itc.enrollment_scheduling_system.model.User;
import edu.itc.enrollment_scheduling_system.repository.UserRepository;
import edu.itc.enrollment_scheduling_system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
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
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        User user = userService.getCurrentUser(authentication);
        if (user == null) return "redirect:/login";

        UserProfileForm form = userService.toForm(user);

        model.addAttribute("form", form);
        return "user-profile";
    }

    @PostMapping("/profile")
    public String updateProfile(Authentication authentication,
                                @Valid @ModelAttribute("form") UserProfileForm form,
                                BindingResult bindingResult,
                                Model model) {
        User user = userService.getCurrentUser(authentication);
        if (user == null) return "redirect:/login";

        // Enforce non-editable full name in server side by overriding
        form.setFullName(user.getFullName());

        // Call changePassword
        userService.changePassword(user, form, bindingResult);

        if (bindingResult.hasErrors()) {
            return "user-profile";
        }

        // Update editable fields
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        user.setBio(form.getBio());
        user.setPhone(form.getPhone());
        user.setAddress(form.getAddress());
        userRepository.save(user);

        // Refresh model
        model.addAttribute("form", form);
        model.addAttribute("success", "Profile updated successfully");
        return "user-profile";
    }
}
