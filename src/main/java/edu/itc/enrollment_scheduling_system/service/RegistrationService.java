package edu.itc.enrollment_scheduling_system.service;

import edu.itc.enrollment_scheduling_system.dto.RegistrationDTO;
import edu.itc.enrollment_scheduling_system.model.Account;
import lombok.RequiredArgsConstructor;
import edu.itc.enrollment_scheduling_system.repository.AccountRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('GUEST')")
    public void registerUser(RegistrationDTO form) {
        if (accountRepository.findByUsername(form.username()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (accountRepository.findByEmail(form.email()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        accountRepository.save(new Account(form, passwordEncoder.encode(form.password())));
    }
}