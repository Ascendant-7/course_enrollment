package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.repository.CourseRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CourseRepository courseRepository;

    public AdminController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin-dashboard";
    }

    @PostMapping("/courses/save")
    public String saveCourse(
            @Valid @ModelAttribute("course") Course course,
            BindingResult result) {

        // Ensure course is not null
        Objects.requireNonNull(course, "Course must not be null");

        // Check for validation errors
        if (result.hasErrors()) {
            return "admin/course-form";
        }

        // Save the course
        courseRepository.save(course);

        return "redirect:/admin/courses";
    }
}
