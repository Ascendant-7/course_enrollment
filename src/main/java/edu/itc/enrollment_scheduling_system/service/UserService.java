package edu.itc.enrollment_scheduling_system.service;

import edu.itc.enrollment_scheduling_system.dto.UserProfileForm;
import edu.itc.enrollment_scheduling_system.model.User;
import edu.itc.enrollment_scheduling_system.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public long countApprovedUsers() {
        return userRepository.countByApprovedTrue();
    }

    public User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        String username = authentication.getName();
        if (username == null || username.equals("anonymousUser")) {
            return null;
        }
        
        return userRepository.findByUsername(username).orElse(null);
    }

    public UserProfileForm toForm(User user) {
        UserProfileForm form = new UserProfileForm();
        form.setUsername(user.getUsername());
        form.setEmail(user.getEmail());
        form.setFullName(user.getFullName());
        form.setBio(user.getBio());
        form.setPhone(user.getPhone());
        form.setAddress(user.getAddress());
        return form;
    }

    public void changePassword(User user, UserProfileForm form, BindingResult bindingResult) {
        // Only attempt to change password if new password is provided
        if (form.getNewPassword() != null && !form.getNewPassword().trim().isEmpty()) {
            // Validate current password
            if (form.getCurrentPassword() == null || form.getCurrentPassword().trim().isEmpty()) {
                bindingResult.rejectValue("currentPassword", "error.currentPassword", 
                    "Current password is required to change password");
                return;
            }

            // Check if current password matches
            if (!passwordEncoder.matches(form.getCurrentPassword(), user.getPassword())) {
                bindingResult.rejectValue("currentPassword", "error.currentPassword", 
                    "Current password is incorrect");
                return;
            }

            // Validate new password
            if (form.getNewPassword().length() < 6) {
                bindingResult.rejectValue("newPassword", "error.newPassword", 
                    "New password must be at least 6 characters");
                return;
            }

            // Validate password confirmation
            if (form.getConfirmPassword() == null || 
                !form.getNewPassword().equals(form.getConfirmPassword())) {
                bindingResult.rejectValue("confirmPassword", "error.confirmPassword", 
                    "Passwords do not match");
                return;
            }

            // Update password
            user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        }
    }
}