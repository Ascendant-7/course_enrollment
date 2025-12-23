package edu.itc.enrollment_scheduling_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
    name = "courses",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "code")
    }
)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Course code (e.g., CS101)
    @NotBlank(message = "Course code is required")
    @Size(min = 3, max = 10, message = "Course code must be 3–10 characters")
    @Column(nullable = false, unique = true)
    private String code;

    // Course name
    @NotBlank(message = "Course name is required")
    @Size(max = 100, message = "Course name must not exceed 100 characters")
    @Column(nullable = false)
    private String name;

    // Description (optional)
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    // Capacity
    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Column(nullable = false)
    private Integer capacity;

    // Relationship
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Enrollment> enrollments = new HashSet<>();

    // ==========================
    // Constructors
    // ==========================
    public Course() {}

    // ==========================
    // Getters & Setters
    // ==========================
    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(Set<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    // ==========================
    // Business Logic
    // ==========================
    @Transient
    public Integer getAvailableSeats() {
        if (capacity == null || enrollments == null) {
            return 0;
        }
        return Math.max(0, capacity - enrollments.size());
    }
}
