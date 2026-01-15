package edu.itc.enrollment_scheduling_system.service;

import edu.itc.enrollment_scheduling_system.dto.PasswordChangeForm;
import edu.itc.enrollment_scheduling_system.model.Account;
import edu.itc.enrollment_scheduling_system.repository.AccountRepository;
import edu.itc.enrollment_scheduling_system.util.CustomStringUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

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
        PasswordChangeForm form,
        Account account,
        BindingResult result
    ) {
        if (CustomStringUtil.trimToNull(form.newPassword()) == null) return;

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
}