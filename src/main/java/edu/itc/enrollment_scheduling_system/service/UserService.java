package edu.itc.enrollment_scheduling_system.service;

import edu.itc.enrollment_scheduling_system.dto.UserProfileForm;
import edu.itc.enrollment_scheduling_system.model.User;
import edu.itc.enrollment_scheduling_system.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username).orElse(null);
    }

    public UserProfileForm toForm(User user) {
        UserProfileForm form = new UserProfileForm();
        form.setUsername(user.getUsername());
        form.setFullName(user.getFullName());
        form.setEmail(user.getEmail());
        form.setBio(user.getBio());
        form.setPhone(user.getPhone());
        form.setAddress(user.getAddress());
        form.setRoles(user.getRoles().stream().map(role -> role.getName()).toList());
        form.setEnabled(user.isEnabled());
        return form;
    }

    public void changePassword(User user, UserProfileForm form, BindingResult bindingResult) {
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

        if (changingPassword) {
            user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        }
    }
}