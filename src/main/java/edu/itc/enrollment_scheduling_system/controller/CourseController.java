package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.config.AccountDetails;
import edu.itc.enrollment_scheduling_system.dto.CreateUpdateCourseDTO;
import edu.itc.enrollment_scheduling_system.model.Account;
import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.repository.CourseRepository;
import edu.itc.enrollment_scheduling_system.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseRepository courseRepository;
    private final EnrollmentService enrollmentService;

    @GetMapping
    public String listCourses(
        Model model,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Page<Course> coursePage = courseRepository.findAll(
            PageRequest.of(page, size)
        );
        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        return "course/list";
    }

    @GetMapping("/{id}/enroll")
    public String enrollInCourse(
        @PathVariable @NonNull Integer id,
        @AuthenticationPrincipal AccountDetails accountDetails,
        RedirectAttributes redirection
    ) {
        Account student = accountDetails.getAccount();

        try {
            enrollmentService.enrollCourse(student, courseRepository.getReferenceById(id));
            redirection.addFlashAttribute("success", "Successfully enrolled in course!");
        } catch (Exception e) {
            redirection.addFlashAttribute("error", "Unabled to enroll in course: " + e.getMessage());
        }

        return "redirect:/student/dashboard";
    }

    @PostMapping("/{id}/drop")
    public String dropCourse(
        @PathVariable @NonNull Integer id,
        @AuthenticationPrincipal AccountDetails accountDetails,
        RedirectAttributes redirection
    ) {
        Account student = accountDetails.getAccount();

        try {
            enrollmentService.dropCourse(student, courseRepository.getReferenceById(id));
            redirection.addFlashAttribute("success", "Successfully dropped course!");
        } catch (Exception e) {
            redirection.addFlashAttribute("error", "Unable to drop course: " + e.getMessage());
        }

        return "redirect:/student/dashboard";
    }



    @GetMapping("/create-course")
    public String createForm() {
        return "course/form";
    }

    @PostMapping("/create-course")
    public String createSubmit(
        @Valid @ModelAttribute("form") CreateUpdateCourseDTO form,
        BindingResult result,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) return "course/form";

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

        model.addAttribute("form", new CreateUpdateCourseDTO(course));
        model.addAttribute("courseId", id);
        return "course/form";
    }

    @PostMapping("/edit-course/{id}")
    public String editSubmit(
        @PathVariable @NonNull Integer id,
        @Valid @ModelAttribute("form") CreateUpdateCourseDTO form,
        BindingResult result,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) return "course/form";

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