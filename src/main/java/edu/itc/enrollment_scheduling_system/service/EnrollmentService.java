package edu.itc.enrollment_scheduling_system.service;

import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.model.Enrollment;
import edu.itc.enrollment_scheduling_system.model.Account;
import edu.itc.enrollment_scheduling_system.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public void enrollCourse(Account student, Course course) {

        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            throw new RuntimeException("This student has already registered in this course.");
        }

        enrollmentRepository.save(new Enrollment(student, course));
    }

    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public void dropCourse(
        Account student,
        Course course
    ) {

        enrollmentRepository.findByStudentAndCourse(student, course)
            .ifPresentOrElse(
                enrollmentRepository::delete,
                () -> { throw new RuntimeException("Enrollment not found"); }
            );
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN', 'TEACHER')")
    public boolean hasEnrolled(Account student, Course course) {
        return enrollmentRepository.existsByStudentAndCourse(student, course);
    }
}
