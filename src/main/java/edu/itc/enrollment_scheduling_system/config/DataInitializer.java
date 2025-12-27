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
        var teacherRole = roles.findByName("ROLE_TEACHER").orElseGet(() -> roles.save(new Role("ROLE_TEACHER")));
        var studentRole = roles.findByName("ROLE_STUDENT").orElseGet(() -> roles.save(new Role("ROLE_STUDENT")));

        if (!users.existsByUsername("admin")) {
            var admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin123"));
            admin.setApproved(true); // Auto-approve admin
            admin.getRoles().add(adminRole);
            users.save(admin);
        }

        if (!users.existsByUsername("student")) {
            var student = new User();
            student.setUsername("student");
            student.setPassword(encoder.encode("student123"));
            student.setApproved(true); // For testing
            student.getRoles().add(studentRole);
            users.save(student);
        }

        if (!users.existsByUsername("teacher")) {
            var teacher = new User();
            teacher.setUsername("teacher");
            teacher.setPassword(encoder.encode("teacher123"));
            teacher.setApproved(true); // For testing
            teacher.getRoles().add(teacherRole);
            users.save(teacher);
        }
    }
}