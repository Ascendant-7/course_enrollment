package edu.itc.enrollment_scheduling_system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import edu.itc.enrollment_scheduling_system.dto.CourseForm;

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

    public Course(CourseForm form) {
        this.code = form.code();
        this.name = form.name();
        this.credits = form.credits();
        this.capacity = form.capacity();
        this.description = form.description();
    }

    public void update(CourseForm form) {
        if (!Objects.equals(this.code, form.code()))
            this.code = form.code();

        if (!Objects.equals(this.name, form.name()))
            this.name = form.name();

        if (!Objects.equals(this.credits, form.credits()))
            this.credits = form.credits();

        if (!Objects.equals(this.capacity, form.capacity()))
            this.capacity = form.capacity();
        if (!Objects.equals(this.description, form.description())) 
            this.description = form.description();
    }

    // countEnrollments in EnrollmentRepository
}
