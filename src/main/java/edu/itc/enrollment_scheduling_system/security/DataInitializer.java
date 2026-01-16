package edu.itc.enrollment_scheduling_system.security;

import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.model.Role;
import edu.itc.enrollment_scheduling_system.model.Account;
import edu.itc.enrollment_scheduling_system.repository.CourseRepository;
import edu.itc.enrollment_scheduling_system.repository.RoleRepository;
import edu.itc.enrollment_scheduling_system.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.lang.NonNull;
import java.util.Objects;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final CourseRepository courseRepository;

    public DataInitializer(RoleRepository roleRepository,
                          AccountRepository accountRepository,
                          PasswordEncoder encoder,
                          CourseRepository courseRepository) {
        this.roleRepository = roleRepository;
        this.accountRepository = accountRepository;
        this.encoder = encoder;
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        System.out.println("=== DATA INITIALIZER STARTING ===");
        
        // Create roles
        Role adminRole = roleRepository.findByName("ADMIN")
            .orElseGet(() -> {
                Role role = new Role();
                role.setName("ADMIN");
                Role savedRole = roleRepository.save(role);
                System.out.println("✅ Created ADMIN role with ID: " + savedRole.getId());
                return savedRole;
            });

        Role teacherRole = roleRepository.findByName("TEACHER")
            .orElseGet(() -> {
                Role role = new Role();
                role.setName("TEACHER");
                Role savedRole = roleRepository.save(role);
                System.out.println("✅ Created TEACHER role with ID: " + savedRole.getId());
                return savedRole;
            });

        Role studentRole = roleRepository.findByName("STUDENT")
            .orElseGet(() -> {
                Role role = new Role();
                role.setName("STUDENT");
                Role savedRole = roleRepository.save(role);
                System.out.println("✅ Created STUDENT role with ID: " + savedRole.getId());
                return savedRole;
            });

        // Create admin user
        if (!accountRepository.existsByUsername("admin")) {
            Account admin = new Account();
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setPassword(encoder.encode("admin123"));
            admin.setApproved(true);
            admin.setEnabled(true);
            admin.getRoles().add(adminRole);
            
            Account savedAdmin = accountRepository.save(admin);
            System.out.println("✅ Admin user created:");
            System.out.println("   Username: admin");
            System.out.println("   Password: admin123");
            System.out.println("   ID: " + savedAdmin.getId());
            System.out.println("   Roles: " + savedAdmin.getRoles());
            System.out.println("   Approved: " + savedAdmin.isApproved());
            System.out.println("   Enabled: " + savedAdmin.isEnabled());
        } else {
            Account admin = accountRepository.findByUsername("admin").orElse(null);
            System.out.println("ℹ️ Admin user already exists:");
            System.out.println("   ID: " + admin.getId());
            System.out.println("   Roles: " + admin.getRoles());
            System.out.println("   Approved: " + admin.isApproved());
            System.out.println("   Enabled: " + admin.isEnabled());
        }

        // Create teacher user
        if (!accountRepository.existsByUsername("teacher")) {
            Account teacher = new Account();
            teacher.setUsername("teacher");
            teacher.setEmail("teacher@example.com");
            teacher.setFirstName("John");
            teacher.setLastName("Doe");
            teacher.setPassword(encoder.encode("teacher123"));
            teacher.setApproved(true);
            teacher.setEnabled(true);
            teacher.getRoles().add(teacherRole);
            accountRepository.save(teacher);
            System.out.println("✅ Teacher user created: username=teacher, password=teacher123");
        }

        // Create student user
        if (!accountRepository.existsByUsername("student")) {
            Account student = new Account();
            student.setUsername("student");
            student.setEmail("student@example.com");
            student.setFirstName("Jane");
            student.setLastName("Smith");
            student.setPassword(encoder.encode("student123"));
            student.setApproved(true);
            student.setEnabled(true);
            student.getRoles().add(studentRole);
            accountRepository.save(student);
            System.out.println("✅ Student user created: username=student, password=student123");
        }

        // Create sample courses
        if (courseRepository.count() == 0) {
            Account teacher = accountRepository.findByUsername("teacher").orElse(null);

            Course[] sampleCourses = {
                createCourse("CS101", "Introduction to Computer Science", 
                    "Learn the fundamentals of computer science including algorithms, data structures, and programming.", 
                    3, 30, teacher),
                createCourse("CS201", "Data Structures and Algorithms", 
                    "Advanced study of data structures, algorithm design, and complexity analysis.", 
                    4, 25, teacher),
                createCourse("CS301", "Database Systems", 
                    "Comprehensive coverage of database design, SQL, and database management systems.", 
                    3, 20, teacher),
                createCourse("CS401", "Software Engineering", 
                    "Software development lifecycle, project management, and modern development practices.", 
                    4, 15, teacher),
                createCourse("CS302", "Web Development", 
                    "Full-stack web development using modern frameworks and technologies.", 
                    3, 28, teacher),
                createCourse("CS303", "Mobile App Development", 
                    "Build native and cross-platform mobile applications.", 
                    3, 22, teacher)
            };

            for (Course course : sampleCourses) {
                courseRepository.save(Objects.requireNonNull(course, "course must not be null"));
            }
            System.out.println("✅ Sample courses created");
        }
        
        System.out.println("=== DATA INITIALIZER COMPLETE ===");
    }

    private @NonNull Course createCourse(String code, String name, String description, 
                               int credits, int capacity, Account teacher) {
        Course course = new Course();
        course.setCode(code);
        course.setName(name);
        course.setDescription(description);
        course.setCredits(credits);
        course.setCapacity(capacity);
        course.setMaxStudents(capacity);
        course.setEnrolledCount(0);
        course.setDepartment("Computer Science");
        course.setTeacher(teacher);
        return course;
    }
}