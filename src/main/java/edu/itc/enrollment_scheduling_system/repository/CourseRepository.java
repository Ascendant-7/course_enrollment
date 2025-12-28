package edu.itc.enrollment_scheduling_system.repository;

import edu.itc.enrollment_scheduling_system.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByOrderByCodeAsc(); // Add this method
}
