package edu.itc.enrollment_scheduling_system.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.itc.enrollment_scheduling_system.model.Classroom;
import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.model.Account;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    
    @Query("""
            SELECT c FROM Classroom c
            WHERE (LOWER(c.code) = LOWER(:code))
            """)
    Optional<Classroom> findByCode(String code);
    
    @Query("""
            SELECT c FROM Classroom c
            WHERE (:teacher IS NULL OR c.teacher = :teacher)
            AND (:course IS NULL OR c.course = :course)
            """)
    Page<Classroom> search(
        Account teacher,
        Course course,
        Pageable pageable
    );
}