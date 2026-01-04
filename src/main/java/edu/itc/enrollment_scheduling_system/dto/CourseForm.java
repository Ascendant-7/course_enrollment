package edu.itc.enrollment_scheduling_system.dto;

import jakarta.validation.constraints.*;

public class CourseForm {

    @NotBlank(message = "Course code is required")
    @Size(max = 10, message = "Course code must not exceed 10 characters")
    private String code;

    @NotBlank(message = "Course name is required")
    @Size(max = 100, message = "Course name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Credits is required")
    @Min(value = 1, message = "Credits must be at least 1")
    @Max(value = 6, message = "Credits must not exceed 6")
    private Integer credits;

    @NotNull(message = "Max students is required")
    @Min(value = 1, message = "Max students must be at least 1")
    private Integer maxStudents;

    // Getters and setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public Integer getMaxStudents() { return maxStudents; }
    public void setMaxStudents(Integer maxStudents) { this.maxStudents = maxStudents; }
}