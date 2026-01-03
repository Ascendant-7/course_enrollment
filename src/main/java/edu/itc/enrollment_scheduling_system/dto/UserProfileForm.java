package edu.itc.enrollment_scheduling_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UserProfileForm {
    @NotBlank(message = "Username is required")
    private String username;

    private String fullName; // read-only in UI

    @Email(message = "Invalid email")
    private String email;

    private String bio;

    private String phone;

    private String address;

    private List<String> roles;

    private boolean enabled;

    // password change fields
    private String oldPassword; // required if newPassword is provided

    @Size(min = 6, message = "New password must be at least 6 characters")
    private String newPassword;

    private String confirmNewPassword;
}
