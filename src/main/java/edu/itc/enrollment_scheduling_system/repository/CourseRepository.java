package edu.itc.enrollment_scheduling_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.itc.enrollment_scheduling_system.model.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    
}
