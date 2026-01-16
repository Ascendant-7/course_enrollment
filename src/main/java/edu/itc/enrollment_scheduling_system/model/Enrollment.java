package edu.itc.enrollment_scheduling_system.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    @Column(updatable = false, insertable = false)
    private LocalDateTime createdAt;
    @Column(updatable = false, insertable = false)
    private LocalDateTime updatedAt;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Account student;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
