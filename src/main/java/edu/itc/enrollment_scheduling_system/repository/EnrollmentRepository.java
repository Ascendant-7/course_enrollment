package edu.itc.enrollment_scheduling_system.repository;

import edu.itc.enrollment_scheduling_system.model.Enrollment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import edu.itc.enrollment_scheduling_system.model.Account;
import edu.itc.enrollment_scheduling_system.model.Course;
import java.util.Optional;





public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    boolean existsByStudentAndCourse(Account student, Course course);
    
    Optional<Enrollment> findByStudentAndCourse(Account account, Course course);
    Page<Enrollment> findByStudent(Account student, Pageable pageable);
    Page<Enrollment> findByCourse(Course course, Pageable pageable);
}
