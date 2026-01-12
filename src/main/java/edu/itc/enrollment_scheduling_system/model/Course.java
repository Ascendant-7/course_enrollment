package edu.itc.enrollment_scheduling_system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@Data
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull private String code;
    @NonNull private String name;
    @NonNull private Integer credits;
    @NonNull private Integer capacity;

    private String description;

    @Column(nullable = false)
    private Boolean enabled;

    @OneToMany(mappedBy = "course")
    private Set<Enrollment> enrollments = new HashSet<>();

    // countEnrollments in EnrollmentRepository
}
