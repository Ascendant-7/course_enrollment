package edu.itc.enrollment_scheduling_system.cli;

import edu.itc.enrollment_scheduling_system.dto.RegistrationDTO;
import lombok.RequiredArgsConstructor;
import edu.itc.enrollment_scheduling_system.service.AccountService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AccountService accountService;

    @Override
    @Transactional
    public void run(String... args) {
        System.out.println("=== DATA INITIALIZER STARTING ===");

        // Create superadmin
        accountService.createUser(new RegistrationDTO(
            "ascendant",
            "angpanha@gmail.com",
            "123456",
            "Ang",
            "Panha"
        ));

        System.out.println("=== DATA INITIALIZER COMPLETE ===");
    }
}