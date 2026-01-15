package edu.itc.enrollment_scheduling_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordChangeForm(
    @NotBlank(message = "Current password is required")
    String currentPassword,

    @Size(min = 6, message = "New password must be at least 6 characters")
    String newPassword,

    @NotBlank(message = "Password confirmation is required")
    String confirmPassword
) {}
