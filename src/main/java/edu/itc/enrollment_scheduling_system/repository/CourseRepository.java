package edu.itc.enrollment_scheduling_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.itc.enrollment_scheduling_system.model.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    
    boolean existsByCode(String code);
    Optional<Course> findByCode(String code);
}
