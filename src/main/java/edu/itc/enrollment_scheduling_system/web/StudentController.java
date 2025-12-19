package edu.itc.enrollment_scheduling_system.web;

import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.model.Enrollment;
import edu.itc.enrollment_scheduling_system.model.User;
import edu.itc.enrollment_scheduling_system.repository.UserRepository;
import edu.itc.enrollment_scheduling_system.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "student-dashboard";
    }

    @GetMapping("/courses")
    public String viewCourses(Model model, Authentication authentication) {
        User student = userRepository.findByUsername(authentication.getName()).orElse(null);
        
        if (student == null) {
            return "redirect:/login";
        }

        List<Course> allCourses = enrollmentService.getAllCourses();
        List<Enrollment> myEnrollments = enrollmentService.getStudentEnrollments(student);

        model.addAttribute("courses", allCourses);
        model.addAttribute("enrollments", myEnrollments);
        model.addAttribute("student", student);
        
        return "course-enrollment";
    }

    @PostMapping("/enroll/{courseId}")
    public String enrollInCourse(@PathVariable Long courseId, 
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        User student = userRepository.findByUsername(authentication.getName()).orElse(null);
        
        if (student == null) {
            return "redirect:/login";
        }

        String message = enrollmentService.enrollStudent(student, courseId);
        redirectAttributes.addFlashAttribute("message", message);
        
        return "redirect:/student/courses";
    }

    @PostMapping("/drop/{courseId}")
    public String dropCourse(@PathVariable Long courseId, 
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {
        User student = userRepository.findByUsername(authentication.getName()).orElse(null);
        
        if (student == null) {
            return "redirect:/login";
        }

        String message = enrollmentService.dropCourse(student, courseId);
        redirectAttributes.addFlashAttribute("message", message);
        
        return "redirect:/student/courses";
    }
}
