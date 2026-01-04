package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.repository.CourseRepository;
import edu.itc.enrollment_scheduling_system.repository.EnrollmentRepository;
import edu.itc.enrollment_scheduling_system.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public AdminController(UserRepository userRepository,
                          CourseRepository courseRepository,
                          EnrollmentRepository enrollmentRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            long totalUsers = userRepository.count();
            long totalCourses = courseRepository.count();
            long totalEnrollments = enrollmentRepository.count();
            long pendingApprovals = userRepository.countByApprovedFalse();

            model.addAttribute("totalUsers", totalUsers);
            model.addAttribute("totalCourses", totalCourses);
            model.addAttribute("totalEnrollments", totalEnrollments);
            model.addAttribute("pendingApprovals", pendingApprovals);

            return "admin-dashboard";
        } catch (Exception e) {
            System.err.println("Error loading admin dashboard: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error loading dashboard: " + e.getMessage());
            return "error/500";
        }
    }
}
