package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.dto.CourseForm;
import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.repository.CourseRepository;
import jakarta.validation.Valid;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/courses")
public class AdminCourseController {

    private final CourseRepository courseRepository;

    public AdminCourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        return "admin-courses";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("form", new CourseForm());
        return "admin-course-form";
    }

    @PostMapping("/create")
    public String createSubmit(@Valid @ModelAttribute("form") CourseForm form,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) return "admin-course-form";

        Course course = new Course();
        course.setCode(form.getCode());
        course.setName(form.getName());
        course.setDescription(form.getDescription());
        course.setCredits(form.getCredits());
        course.setCapacity(form.getMaxStudents());
        course.setMaxStudents(form.getMaxStudents());
        
        courseRepository.save(course);
        redirectAttributes.addFlashAttribute("message", "Course created successfully");
        return "redirect:/admin/courses";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable @NonNull Long id, Model model) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        CourseForm form = new CourseForm();
        form.setCode(course.getCode());
        form.setName(course.getName());
        form.setDescription(course.getDescription());
        form.setCredits(course.getCredits());
        form.setMaxStudents(course.getMaxStudents());

        model.addAttribute("form", form);
        model.addAttribute("courseId", id);
        return "admin-course-form";
    }

    @PostMapping("/{id}/edit")
    public String editSubmit(@PathVariable @NonNull Long id,
                            @Valid @ModelAttribute("form") CourseForm form,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin-course-form";
        }

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        course.setCode(form.getCode());
        course.setName(form.getName());
        course.setDescription(form.getDescription());
        course.setCredits(form.getCredits());
        course.setCapacity(form.getMaxStudents());
        course.setMaxStudents(form.getMaxStudents());

        courseRepository.save(course);
        redirectAttributes.addFlashAttribute("message", "Course updated successfully");
        return "redirect:/admin/courses";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable @NonNull Long id, RedirectAttributes redirectAttributes) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        String courseName = course.getName();
        courseRepository.delete(course);
        
        redirectAttributes.addFlashAttribute("message", "Course '" + courseName + "' deleted successfully");
        return "redirect:/admin/courses";
    }

    @GetMapping("/{id}/teachers")
    public String viewTeachers(@PathVariable @NonNull Long id, Model model) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        
        model.addAttribute("course", course);
        
        return "admin-course-teachers";
    }

    @GetMapping("/{id}/students")
    public String viewStudents(@PathVariable @NonNull Long id, Model model) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        
        model.addAttribute("course", course);
        
        return "admin-course-students";
    }
}