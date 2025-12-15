package edu.itc.enrollment_scheduling_system.config;

import edu.itc.enrollment_scheduling_system.model.Role;
import edu.itc.enrollment_scheduling_system.model.User;
import edu.itc.enrollment_scheduling_system.repository.RoleRepository;
import edu.itc.enrollment_scheduling_system.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roles;
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public DataInitializer(RoleRepository roles, UserRepository users, PasswordEncoder encoder) {
        this.roles = roles;
        this.users = users;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        var adminRole = roles.findByName("ROLE_ADMIN").orElseGet(() -> roles.save(new Role("ROLE_ADMIN")));
        roles.findByName("ROLE_TEACHER").orElseGet(() -> roles.save(new Role("ROLE_TEACHER")));
        roles.findByName("ROLE_STUDENT").orElseGet(() -> roles.save(new Role("ROLE_STUDENT")));

        if (!users.existsByUsername("admin")) {
            var admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin123")); // change in real use
            admin.getRoles().add(adminRole);
            users.save(admin);
        }
    }
}