package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.model.User;
import edu.itc.enrollment_scheduling_system.repository.CourseRepository;
import edu.itc.enrollment_scheduling_system.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public AdminController(UserRepository userRepository, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<User> pendingUsers = userRepository.findByApprovedFalse();
        List<Course> allCourses = courseRepository.findAll();
        long totalUsers = userRepository.count();
        long approvedUsers = userRepository.countByApprovedTrue();
        
        model.addAttribute("pendingUsers", pendingUsers);
        model.addAttribute("pendingCount", pendingUsers.size());
        model.addAttribute("totalCourses", allCourses.size());
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("approvedUsers", approvedUsers);
        
        return "admin-dashboard";
    }
}