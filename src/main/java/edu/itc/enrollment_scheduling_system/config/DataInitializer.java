package edu.itc.enrollment_scheduling_system.config;

import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.model.Role;
import edu.itc.enrollment_scheduling_system.model.User;
import edu.itc.enrollment_scheduling_system.repository.CourseRepository;
import edu.itc.enrollment_scheduling_system.repository.RoleRepository;
import edu.itc.enrollment_scheduling_system.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roles;
    private final UserRepository users;
    private final CourseRepository courses;
    private final PasswordEncoder encoder;

    public DataInitializer(RoleRepository roles, UserRepository users, CourseRepository courses, PasswordEncoder encoder) {
        this.roles = roles;
        this.users = users;
        this.courses = courses;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        // Initialize roles
        var adminRole = roles.findByName("ROLE_ADMIN").orElseGet(() -> roles.save(new Role("ROLE_ADMIN")));
        var teacherRole = roles.findByName("ROLE_TEACHER").orElseGet(() -> roles.save(new Role("ROLE_TEACHER")));
        var studentRole = roles.findByName("ROLE_STUDENT").orElseGet(() -> roles.save(new Role("ROLE_STUDENT")));

        // Create admin user
        if (!users.existsByUsername("admin")) {
            var admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin123"));
            admin.getRoles().add(adminRole);
            users.save(admin);
        }

        // Create sample student user
        if (!users.existsByUsername("student")) {
            var student = new User();
            student.setUsername("student");
            student.setPassword(encoder.encode("student123"));
            student.getRoles().add(studentRole);
            users.save(student);
        }

        // Create sample teacher user
        if (!users.existsByUsername("teacher")) {
            var teacher = new User();
            teacher.setUsername("teacher");
            teacher.setPassword(encoder.encode("teacher123"));
            teacher.getRoles().add(teacherRole);
            users.save(teacher);
        }

        // Create sample courses
        if (courses.count() == 0) {
            var teacher = users.findByUsername("teacher").orElse(null);

            Course course1 = new Course("CS101", "Introduction to Computer Science", 
                "Learn the fundamentals of computer science including algorithms, data structures, and programming.", 
                3, 30);
            course1.setTeacher(teacher);
            courses.save(course1);

            Course course2 = new Course("CS201", "Data Structures and Algorithms", 
                "Advanced study of data structures, algorithm design, and complexity analysis.", 
                4, 25);
            course2.setTeacher(teacher);
            courses.save(course2);

            Course course3 = new Course("CS301", "Database Systems", 
                "Comprehensive coverage of database design, SQL, and database management systems.", 
                3, 20);
            course3.setTeacher(teacher);
            courses.save(course3);

            Course course4 = new Course("CS401", "Software Engineering", 
                "Software development lifecycle, project management, and modern development practices.", 
                4, 15);
            course4.setTeacher(teacher);
            courses.save(course4);

            Course course5 = new Course("CS302", "Web Development", 
                "Full-stack web development using modern frameworks and technologies.", 
                3, 28);
            course5.setTeacher(teacher);
            courses.save(course5);

            Course course6 = new Course("CS303", "Mobile App Development", 
                "Build native and cross-platform mobile applications.", 
                3, 22);
            course6.setTeacher(teacher);
            courses.save(course6);
        }
    }
}