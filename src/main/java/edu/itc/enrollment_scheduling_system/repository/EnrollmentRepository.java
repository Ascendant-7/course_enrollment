package edu.itc.enrollment_scheduling_system.repository;

import edu.itc.enrollment_scheduling_system.model.Enrollment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.itc.enrollment_scheduling_system.model.Account;
import edu.itc.enrollment_scheduling_system.model.Course;
import java.util.Optional;




public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    boolean existsByStudentAndCourse(Account student, Course course);

    Optional<Enrollment> findByStudentAndCourse(Account student, Course course);
    
    @Query("""
            SELECT e FROM Enrollment e
            WHERE (:student IS NULL OR e.student = :student)
            AND (:course IS NULL OR c.course = :course)
            """)
    Page<Enrollment> search(Account student, Course course, Pageable pageable);
}
