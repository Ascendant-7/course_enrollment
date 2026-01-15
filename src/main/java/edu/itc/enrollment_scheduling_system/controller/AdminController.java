package edu.itc.enrollment_scheduling_system.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.itc.enrollment_scheduling_system.dto.CourseForm;
import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.repository.CourseRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin-dashboard")
@RequiredArgsConstructor
public class AdminController {

    private final CourseRepository courseRepository;

    @GetMapping("/")
    public String dashboard(Model model) {
        try {

            // TODO: add more
            return "admin-dashboard";
        } catch (Exception e) {
            System.err.println("Error loading admin dashboard: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error loading dashboard: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/create-course")
    public String createForm() {
        return "create-course"; // TODO: add more
    }

    @PostMapping("/create-course")
    public String createSubmit(
        @Valid @ModelAttribute("form") CourseForm form,
        BindingResult result,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) return "create-course";

        courseRepository.save(new Course(form));
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "Course created successfully"
        );
        return "redirect:/admin-dashboard";
    }

    @GetMapping("/edit-course/{id}")
    public String editForm(
        @PathVariable @NonNull Integer id,
        Model model
    ) {
        Course course = courseRepository.findById(id)
            .orElseThrow(
                () -> new IllegalArgumentException("Course not found!")
            );

        model.addAttribute("form", new CourseForm(course));
        model.addAttribute("courseId", id);
        return "edit-course";
    }

    @PostMapping("/edit-course/{id}")
    public String editSubmit(
        @PathVariable @NonNull Integer id,
        @Valid @ModelAttribute("form") CourseForm form,
        BindingResult result,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) return "edit-course";

        Course course = courseRepository.findById(id)
            .orElseThrow(
                () -> new IllegalArgumentException("Course not found!")
            );

        course.update(form);
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "Course updated successfully"
        );
        return "redirect:/admin-dashboard";
    }

    @PostMapping("/delete-course/{id}")
    public String delete(
        @PathVariable @NonNull Integer id,
        RedirectAttributes redirectAttributes
    ) {
        Course course = courseRepository.findById(id)
            .orElseThrow(
                () -> new IllegalArgumentException("Course not found")
            );

        String courseName = course.getName();
        courseRepository.delete(course);
        
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "Course '" + courseName + "' deleted successfully"
        );
        return "redirect:/admin-dashboard";
    }

}
