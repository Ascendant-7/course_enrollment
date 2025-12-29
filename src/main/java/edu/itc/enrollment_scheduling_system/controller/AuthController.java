
package edu.itc.enrollment_scheduling_system.controller;
import edu.itc.enrollment_scheduling_system.service.RegistrationService;
import edu.itc.enrollment_scheduling_system.service.EnrollmentService;
import edu.itc.enrollment_scheduling_system.model.User;
import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.model.Enrollment;
import edu.itc.enrollment_scheduling_system.repository.UserRepository;
import edu.itc.enrollment_scheduling_system.dto.RegisterForm;
import org.springframework.security.core.Authentication;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
@Controller
public class AuthController {

    private final RegistrationService registrationService;
    private final EnrollmentService enrollmentService;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(RegistrationService registrationService, EnrollmentService enrollmentService, UserRepository userRepository) {
        this.registrationService = registrationService;
        this.enrollmentService = enrollmentService;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // If /register is ADMIN-only in SecurityConfig, only admins can reach this page
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("form", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@Valid @ModelAttribute("form") RegisterForm form,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) return "register";

        try {
            registrationService.register(form);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/courses";
    }

    @GetMapping("/courses")
    public String viewCourses(Model model, @RequestParam(required = false) String search, Authentication authentication) {
        List<Course> allCourses = enrollmentService.getAllCourses();
        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.toLowerCase();
            allCourses = allCourses.stream()
                .filter(course -> (course.getName() != null && course.getName().toLowerCase().contains(searchLower)) ||
                                 (course.getCode() != null && course.getCode().toLowerCase().contains(searchLower)))
                .toList();
        }
        User currentUser = null;
        if (authentication != null && authentication.isAuthenticated()) {
            currentUser = userRepository.findByUsername(authentication.getName()).orElse(null);
        }
        model.addAttribute("courses", allCourses);
        model.addAttribute("search", search);
        // For public view, no student, no enrollments
        model.addAttribute("enrollments", List.of());
        model.addAttribute("student", currentUser);
        model.addAttribute("teacher", (User) null); // Explicitly set teacher to null for anonymous users
        return "course-enrollment";
    }

    @GetMapping("/courses/{id}")
    public String viewCourse(@PathVariable Long id, Model model, Authentication authentication) {
        Course course = enrollmentService.getCourseById(id);
        if (course == null) {
            return "redirect:/courses";
        }
        User currentUser = null;
        if (authentication != null && authentication.isAuthenticated()) {
            currentUser = userRepository.findByUsername(authentication.getName()).orElse(null);
        }

        // Check if current user is a teacher
        boolean isTeacher = false;
        if (authentication != null && authentication.isAuthenticated()) {
            isTeacher = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"));
        }

        model.addAttribute("course", course);
        model.addAttribute("student", currentUser);
        model.addAttribute("isTeacher", isTeacher);
        return "course-detail";
    }

    @GetMapping("/courses/{id}/teachers")
    public String viewCourseTeachers(@PathVariable Long id, Model model, Authentication authentication) {
        Course course = enrollmentService.getCourseById(id);
        if (course == null) {
            return "redirect:/courses";
        }
        User currentUser = null;
        if (authentication != null && authentication.isAuthenticated()) {
            currentUser = userRepository.findByUsername(authentication.getName()).orElse(null);
        }

        // Check if current user is a teacher
        boolean isTeacher = false;
        if (authentication != null && authentication.isAuthenticated()) {
            isTeacher = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"));
        }

        model.addAttribute("course", course);
        model.addAttribute("teacher", course.getTeacher());
        model.addAttribute("student", currentUser);
        model.addAttribute("isTeacher", isTeacher);
        return "course-teachers";
    }
}