package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.repository.CourseRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CourseRepository courseRepository;

    public AdminController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/courses/new")
    public String showCreateCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "admin/course-form";
    }

    @PostMapping("/courses")
    public String saveCourse(
            @Valid @ModelAttribute("course") Course course,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "admin/course-form";
        }

        courseRepository.save(course);
        return "redirect:/admin/courses";
    }
}
