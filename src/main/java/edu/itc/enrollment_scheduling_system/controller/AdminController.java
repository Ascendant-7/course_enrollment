package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.repository.CourseRepository;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CourseRepository courseRepository;

    @Autowired
    public AdminController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // Admin Dashboard
    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin-dashboard";
    }

    // List Courses
    @GetMapping("/courses")
    public String listCourses(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        return "admin-courses";
    }

    // Show Create Course Form
    @GetMapping("/courses/new")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        return "course-form";
    }

    // Create Course with Validation
    @PostMapping("/courses")
    public String createCourse(
            @Valid @ModelAttribute("course") Course course,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        // Bean validation errors
        if (result.hasErrors()) {
            return "course-form";
        }

        // Duplicate course code validation
        if (courseRepository.existsByCode(course.getCode())) {
            result.rejectValue(
                    "code",
                    "error.course",
                    "Course code already exists"
            );
            return "course-form";
        }

        courseRepository.save(course);
        redirectAttributes.addFlashAttribute(
                "message",
                "Course created successfully"
        );

        return "redirect:/admin/courses";
    }
}
