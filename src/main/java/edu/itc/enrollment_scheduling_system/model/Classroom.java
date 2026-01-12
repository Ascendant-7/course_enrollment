package edu.itc.enrollment_scheduling_system.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

@Entity
@Table(name = "classrooms")
@Data
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull private String code;
    
    private LocalDate startDate;
    private LocalDate endDate;

    @Column(updatable = false, insertable = false)
    private LocalDateTime createdAt;
    @Column(updatable = false, insertable = false)
    private LocalDateTime updatedAt;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    // @OneToMany(mappedBy = "classroom")
    // private Set<Attendance> attendances = new Set<>();

    /*
     * don't forget to make these functions in ClassroomRepository:
     * - countAttendance()
     * - isFull()
     */
}