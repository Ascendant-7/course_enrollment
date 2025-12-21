package edu.itc.enrollment_scheduling_system.service;

import edu.itc.enrollment_scheduling_system.model.Role;
import edu.itc.enrollment_scheduling_system.model.User;
import edu.itc.enrollment_scheduling_system.repository.RoleRepository;
import edu.itc.enrollment_scheduling_system.repository.UserRepository;
import edu.itc.enrollment_scheduling_system.dto.RegisterForm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository users, RoleRepository roles, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.roles = roles;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(RegisterForm form) {
        if (form.getUsername() == null || form.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (users.existsByUsername(form.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (form.getPassword() == null || form.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        var studentRole = roles.findByName("ROLE_STUDENT")
                .orElseGet(() -> roles.save(new Role("ROLE_STUDENT")));

        var user = new User();
        user.setUsername(form.getUsername());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.getRoles().add(studentRole);

        users.save(user);
    }
}