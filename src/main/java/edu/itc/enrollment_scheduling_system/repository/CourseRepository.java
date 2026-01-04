package edu.itc.enrollment_scheduling_system.repository;

import edu.itc.enrollment_scheduling_system.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(
        String code, String name, Pageable pageable);
    
    List<Course> findByTeacherId(Long teacherId);
}
