package edu.itc.enrollment_scheduling_system.service;

import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.model.Enrollment;
import edu.itc.enrollment_scheduling_system.model.User;
import edu.itc.enrollment_scheduling_system.repository.CourseRepository;
import edu.itc.enrollment_scheduling_system.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findByOrderByCodeAsc();
    }

    public List<Enrollment> getStudentEnrollments(User student) {
        return enrollmentRepository.findByStudent(student);
    }

    @Transactional
    public String enrollStudent(User student, Long courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        
        if (course == null) {
            return "Course not found";
        }

        // Check if already enrolled
        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            return "Already enrolled in this course";
        }

        // Check capacity
        if (course.getAvailableSeats() <= 0) {
            return "Course is full";
        }

        Enrollment enrollment = new Enrollment(student, course);
        enrollmentRepository.save(enrollment);
        return "Successfully enrolled";
    }

    @Transactional
    public String dropCourse(User student, Long courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        
        if (course == null) {
            return "Course not found";
        }

        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(student, course).orElse(null);
        
        if (enrollment == null) {
            return "Not enrolled in this course";
        }

        enrollment.setStatus(Enrollment.EnrollmentStatus.DROPPED);
        enrollmentRepository.save(enrollment);
        return "Successfully dropped course";
    }

    public boolean isStudentEnrolled(User student, Course course) {
        return enrollmentRepository.existsByStudentAndCourse(student, course);
    }
}
