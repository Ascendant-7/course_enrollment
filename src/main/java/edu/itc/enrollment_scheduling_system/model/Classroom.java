package edu.itc.enrollment_scheduling_system.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "classrooms")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
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
    private Account teacher;

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