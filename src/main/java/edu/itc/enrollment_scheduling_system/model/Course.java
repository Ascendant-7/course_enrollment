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
    private String department;
    private Integer maxStudents;
    private Integer enrolledCount;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @Column(nullable = false)
    private Boolean enabled;

    @OneToMany(mappedBy = "course")
    private Set<Enrollment> enrollments = new HashSet<>();

    // No-arg constructor for JPA
    public Course() {}

    // Constructor for required fields
    public Course(String code, String name, Integer credits, Integer capacity) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.capacity = capacity;
        this.enabled = true;
    }

    // countEnrollments in EnrollmentRepository
}
