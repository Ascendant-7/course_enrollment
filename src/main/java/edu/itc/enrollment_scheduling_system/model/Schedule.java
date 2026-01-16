package edu.itc.enrollment_scheduling_system.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalTime;

@Entity
@Table(name = "schedules")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull private String dayOfWeek;
    @NonNull private LocalTime startTime;
    @NonNull private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;
}