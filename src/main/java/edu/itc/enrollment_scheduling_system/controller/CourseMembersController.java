package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.model.User;
import edu.itc.enrollment_scheduling_system.repository.CourseRepository;
import edu.itc.enrollment_scheduling_system.repository.UserRepository;
import edu.itc.enrollment_scheduling_system.service.EnrollmentService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.lang.NonNull;

import java.util.List;

@Controller
@RequestMapping("/courses/{courseId}")
public class CourseMembersController {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentService enrollmentService;

    public CourseMembersController(CourseRepository courseRepository,
                                  UserRepository userRepository,
                                  EnrollmentService enrollmentService) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/teachers")
    public String viewTeachers(@PathVariable @NonNull Long courseId, Model model, Authentication authentication) {
        Course course = courseRepository.findById(courseId).orElse(null);

        if (course == null) {
            return "redirect:/courses";
        }

        // Check if user is teacher and owns this course
        boolean isTeacher = authentication.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER"));

        if (isTeacher) {
            User teacher = userRepository.findByUsername(authentication.getName()).orElse(null);
            if (teacher == null || !course.getTeacher().getId().equals(teacher.getId())) {
                return "redirect:/teacher/courses?error=unauthorized";
            }
        }

        model.addAttribute("course", course);
        model.addAttribute("teacher", course.getTeacher());

        return "course-teachers";
    }

    @GetMapping("/students")
    public String viewStudents(@PathVariable @NonNull Long courseId, Model model, Authentication authentication) {
        Course course = courseRepository.findById(courseId).orElse(null);

        if (course == null) {
            return "redirect:/courses";
        }

        // Check if user is teacher and owns this course
        boolean isTeacher = authentication.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER"));

        if (isTeacher) {
            User teacher = userRepository.findByUsername(authentication.getName()).orElse(null);
            if (teacher == null || !course.getTeacher().getId().equals(teacher.getId())) {
                return "redirect:/teacher/courses?error=unauthorized";
            }
        }

        List<User> students = enrollmentService.getStudentsInCourse(course);

        model.addAttribute("course", course);
        model.addAttribute("students", students);

        return "course-students";
    }
}