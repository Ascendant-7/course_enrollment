package edu.itc.enrollment_scheduling_system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
@Data
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, insertable = false)
    private LocalDateTime createdAt;
    @Column(updatable = false, insertable = false)
    private LocalDateTime updatedAt;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    // No-arg constructor for JPA
    public Enrollment() {}

    // Constructor for required fields
    public Enrollment(User student, Course course) {
        this.student = student;
        this.course = course;
    }
}
