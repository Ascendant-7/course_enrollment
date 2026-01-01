package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.model.User;
import edu.itc.enrollment_scheduling_system.repository.UserRepository;
import edu.itc.enrollment_scheduling_system.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        User teacher = userRepository.findByUsername(authentication.getName()).orElse(null);

        if (teacher == null) {
            return "redirect:/login";
        }

        // Get courses taught by this teacher
        List<Course> taughtCourses = enrollmentService.getCoursesByTeacher(teacher);

        model.addAttribute("teacher", teacher);
        model.addAttribute("courses", taughtCourses);

        return "teacher-dashboard";
    }

    @GetMapping("/courses")
    public String viewCourses(Model model, @RequestParam(required = false) String search, Authentication authentication) {
        User teacher = userRepository.findByUsername(authentication.getName()).orElse(null);

        if (teacher == null) {
            return "redirect:/login";
        }

        List<Course> allCourses = enrollmentService.getAllCourses();
        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.toLowerCase();
            allCourses = allCourses.stream()
                .filter(course -> (course.getName() != null && course.getName().toLowerCase().contains(searchLower)) ||
                                 (course.getCode() != null && course.getCode().toLowerCase().contains(searchLower)))
                .toList();
        }
        List<Course> taughtCourses = enrollmentService.getCoursesByTeacher(teacher);

        model.addAttribute("courses", allCourses);
        model.addAttribute("taughtCourses", taughtCourses);
        model.addAttribute("teacher", teacher);
        model.addAttribute("search", search);

        return "course-enrollment";
    }

    @GetMapping("/assignments")
    public String viewAssignments(Model model, Authentication authentication) {
        User teacher = userRepository.findByUsername(authentication.getName()).orElse(null);

        if (teacher == null) {
            return "redirect:/login";
        }

        // Get courses taught by this teacher
        List<Course> taughtCourses = enrollmentService.getCoursesByTeacher(teacher);

        model.addAttribute("teacher", teacher);
        model.addAttribute("courses", taughtCourses);

        return "teacher-assignments";
    }
}