package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.model.User;
import edu.itc.enrollment_scheduling_system.repository.CourseRepository;
import edu.itc.enrollment_scheduling_system.repository.UserRepository;
import edu.itc.enrollment_scheduling_system.service.EnrollmentService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentService enrollmentService;

    public StudentController(UserRepository userRepository,
                            CourseRepository courseRepository,
                            EnrollmentService enrollmentService) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        User student = userRepository.findByUsername(authentication.getName()).orElse(null);
        
        if (student == null) {
            return "redirect:/login";
        }

        List<Course> enrolledCourses = enrollmentService.getCoursesByStudentId(student.getId());
        List<Course> allCourses = courseRepository.findAll();
        List<Course> availableCourses = new ArrayList<>();
        
        for (Course course : allCourses) {
            if (!enrollmentService.isStudentEnrolled(student.getId(), course.getId())) {
                availableCourses.add(course);
            }
        }

        model.addAttribute("student", student);
        model.addAttribute("enrolledCourses", enrolledCourses);
        model.addAttribute("availableCourses", availableCourses);
        model.addAttribute("totalEnrolled", enrolledCourses.size());

        return "student-dashboard";
    }

    @GetMapping("/courses")
    public String viewMyCourses(Model model, Authentication authentication) {
        User student = userRepository.findByUsername(authentication.getName()).orElse(null);
        
        if (student == null) {
            return "redirect:/login";
        }

        List<Course> enrolledCourses = enrollmentService.getCoursesByStudentId(student.getId());
        model.addAttribute("student", student);
        model.addAttribute("courses", enrolledCourses);

        return "student-courses";
    }

    @PostMapping("/courses/{courseId}/enroll")
    public String enrollInCourse(@PathVariable Long courseId,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        User student = userRepository.findByUsername(authentication.getName()).orElse(null);
        
        if (student == null) {
            return "redirect:/login";
        }

        try {
            enrollmentService.enrollStudent(student.getId(), courseId);
            redirectAttributes.addFlashAttribute("success", "Successfully enrolled in course!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/student/dashboard";
    }

    @PostMapping("/courses/{courseId}/drop")
    public String dropCourse(@PathVariable Long courseId,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {
        User student = userRepository.findByUsername(authentication.getName()).orElse(null);
        
        if (student == null) {
            return "redirect:/login";
        }

        try {
            enrollmentService.unenrollStudent(student.getId(), courseId);
            redirectAttributes.addFlashAttribute("success", "Successfully dropped course!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/student/dashboard";
    }
}
