package edu.itc.enrollment_scheduling_system.service;

import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.model.Enrollment;
import edu.itc.enrollment_scheduling_system.model.User;
import edu.itc.enrollment_scheduling_system.repository.CourseRepository;
import edu.itc.enrollment_scheduling_system.repository.EnrollmentRepository;
import edu.itc.enrollment_scheduling_system.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;

import java.util.Objects;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                            CourseRepository courseRepository,
                            UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public void enrollStudent(@NonNull Long studentId, @NonNull Long courseId) {
        User student = userRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new RuntimeException("Student already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        enrollmentRepository.save(enrollment);
    }

    public void unenrollStudent(@NonNull Long studentId, @NonNull Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
            .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        
        enrollmentRepository.delete(Objects.requireNonNull(enrollment, "enrollment must not be null"));
    }

    public List<Course> getCoursesByStudentId(@NonNull Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
            .map(Enrollment::getCourse)
            .collect(Collectors.toList());
    }

    public List<Course> getCoursesByTeacherId(@NonNull Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    public List<User> getStudentsInCourse(@NonNull Course course) {
        return enrollmentRepository.findByCourseId(course.getId()).stream()
            .map(Enrollment::getStudent)
            .collect(Collectors.toList());
    }

    public boolean isStudentEnrolled(@NonNull Long studentId, @NonNull Long courseId) {
        return enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Enrollment> getStudentEnrollments(@NonNull User student) {
        return enrollmentRepository.findByStudentId(student.getId());
    }
}
