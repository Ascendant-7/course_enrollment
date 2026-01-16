package edu.itc.enrollment_scheduling_system.cli;

import edu.itc.enrollment_scheduling_system.dto.RegistrationDTO;
import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.model.Role;
import edu.itc.enrollment_scheduling_system.repository.AccountRepository;
import edu.itc.enrollment_scheduling_system.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import edu.itc.enrollment_scheduling_system.service.AccountService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public void run(String... args) {
        System.out.println("=== STARTING DATA INITIALIATION ===");

        // Create superadmin
        initializeUser(
            "ascendant",
            "angpanha@gmail.com",
            "123456",
            "Ang",
            "Panha",
            Role.ADMIN
        );
        initializeUser(
            "anchhy",
            "anchhy@gmail.com",
            "123456",
            "Et",
            "Anchhy",
            Role.STUDENT
        );
        initializeUser(
            "savmoeng",
            "savmoeng@gmail.com",
            "123456",
            "Chi",
            "Sav Moeng",
            Role.TEACHER
        );

        if (!courseRepository.existsByCode("CS101")) {
            courseRepository.save(new Course(
                "CS101", "Introduction to Computer Science",
                3, 550
            ));
        }
        if (!courseRepository.existsByCode("CS201")) {
            courseRepository.save(new Course(
                "CS201", "Data Structures and Algorithms",
                4, 550
            ));
        }
        if (!courseRepository.existsByCode("CS201")) {
            courseRepository.save(new Course(
                "CS301", "Database Systems",
                2, 400
            ));
        }
        
        System.out.println("=== DATA INITIALIZATION COMPLETED ===");
    }

    public void initializeUser(
        String username,
        String email,
        String password,
        String firstname,
        String lastname,
        Role role
    ) {
        System.out.println(accountService.createUser(new RegistrationDTO(
            username,
            email,
            password,
            firstname,
            lastname
        )));

        accountRepository
        .findByEmail(email)
        .ifPresentOrElse(
            account -> {
                account.setRole(role);
                account.setApproved(true);
            },
            () -> { System.out.println("\n\nfailed to initialize "+firstname+" "+lastname+"\n\n"); return; }
        );
    }
}