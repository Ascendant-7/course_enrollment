package edu.itc.enrollment_scheduling_system.service;

import edu.itc.enrollment_scheduling_system.dto.ChangePasswordDTO;
import edu.itc.enrollment_scheduling_system.dto.RegistrationDTO;
import edu.itc.enrollment_scheduling_system.model.Account;
import edu.itc.enrollment_scheduling_system.model.Profile;
import edu.itc.enrollment_scheduling_system.repository.AccountRepository;
import edu.itc.enrollment_scheduling_system.util.StringNormalizer;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public String createUser(RegistrationDTO form) {
        if (accountRepository.existsByEmail(form.email())) {
            return "This email already exists. Unable to create new user.";
        }
        Account account = new Account(
            form.username(),
            form.email(),
            passwordEncoder.encode(form.password())
        );
        accountRepository.save(account);

        Profile profile = new Profile(
            form.firstName(),
            form.lastName(),
            account
        );
        account.setProfile(profile);
        return "user created successfully.";
    }

    public Account getCurrentUser(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        String username = authentication.getName();

        if (username == null || username.equals("anonymousUser")) {
            return null;
        }
        
        return accountRepository.findByUsername(username).orElse(null);
    }

    public void changePassword(
        ChangePasswordDTO form,
        Account account,
        BindingResult result
    ) {
        if (StringNormalizer.trimToNull(form.newPassword()) == null) return;

        if (!passwordEncoder.matches(form.currentPassword(), account.getPasswordHash())) {
            result.rejectValue(
                "currentPassword",
                "invalid",
                "Current password is incorrect"
            );
            return;
        }

        if (!form.newPassword().equals(form.confirmPassword())) {
            result.rejectValue(
                "confirmPassword",
                "mismatch",
                "Passwords do not match"
            );
            return;
        }

        account.setPasswordHash(passwordEncoder.encode(form.newPassword()));
    }

    // dev-only
    public void deleteUser(String email) {

        accountRepository
        .findByEmail(email)
        .ifPresentOrElse(
            account -> {
                accountRepository.delete(
                    Objects.requireNonNull(account)
                );
                System.out.println("user deleted successfully.");
            },
            () -> { System.out.println("unable to delete user."); }
        );
    }
}