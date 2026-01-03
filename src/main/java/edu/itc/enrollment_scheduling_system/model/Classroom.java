package edu.itc.enrollment_scheduling_system.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "classrooms")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_name")
    private String roomName;

   
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

   
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    
    @OneToMany(mappedBy = "classroom")
    private List<Enrollment> enrollments;

    
}