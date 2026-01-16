package edu.itc.enrollment_scheduling_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateAccountDTO(

    @NotNull(message = "User ID is required")
    Long id,

    @NotBlank(message = "Username is required")
    String username,

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email,

    @NotBlank(message = "First name is required")
    String firstName,

    @NotBlank(message = "Last name is required")
    String lastName,

    String phone,
    String address,
    String bio,

    @NotNull(message = "Please select a role")
    Long roleId,

    Boolean enabled,
    Boolean approved,

    // Password fields (optional - only if admin wants to change password)
    String newPassword,
    String confirmPassword
) {}
