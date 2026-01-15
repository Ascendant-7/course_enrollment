package edu.itc.enrollment_scheduling_system.dto;

import edu.itc.enrollment_scheduling_system.model.Course;
import jakarta.validation.constraints.*;

public record CourseForm (

    @NotBlank(message = "Course code is required")
    @Size(max = 10, message = "Course code must not exceed 10 characters")
    String code,

    @NotBlank(message = "Course name is required")
    @Size(max = 100, message = "Course name must not exceed 100 characters")
    String name,

    @Size(max = 500, message = "Description must not exceed 500 characters")
    String description,

    @NotNull(message = "Credits is required")
    @Min(value = 1, message = "Credits must be at least 1")
    @Max(value = 6, message = "Credits must not exceed 6")
    Integer credits,

    @NotNull(message = "capacity is required")
    @Min(value = 1, message = "Max students must be at least 1")
    Integer capacity
) {
    public CourseForm(Course course) {
        this(
            course.getCode(),
            course.getName(),
            course.getDescription(),
            course.getCredits(),
            course.getCapacity()
        );
    }
}