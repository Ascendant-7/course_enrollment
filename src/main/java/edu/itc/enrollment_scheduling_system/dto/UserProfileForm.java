package edu.itc.enrollment_scheduling_system.dto;

import edu.itc.enrollment_scheduling_system.model.Profile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserProfileForm(

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    String firstName,

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    String lastName,

    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{7,15}$", message = "Phone number must be valid")
    String phone,

    @Size(max = 200, message = "Bio cannot exceed 200 characters")
    String bio,

    @Size(max = 200, message = "Address cannot exceed 200 characters")
    String address

) {
    public UserProfileForm(Profile profile) {
        this(
            profile.getFirstName(),
            profile.getLastName(),
            profile.getPhone(),
            profile.getBio(),
            profile.getAddress()
        );
    }
}

