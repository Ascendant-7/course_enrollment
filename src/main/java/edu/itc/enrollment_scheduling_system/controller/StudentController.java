package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.model.Enrollment;
import edu.itc.enrollment_scheduling_system.model.Account;
import edu.itc.enrollment_scheduling_system.repository.EnrollmentRepository;
import edu.itc.enrollment_scheduling_system.security.AccountDetails;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final EnrollmentRepository enrollmentRepository;

    @GetMapping("/dashboard")
    public String dashboard(
        Model model,
        @AuthenticationPrincipal AccountDetails accountDetails,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Account student = accountDetails.getAccount();

        Page<Enrollment> enrollments = 
        enrollmentRepository.search(student, null, PageRequest.of(page, size));

        model.addAttribute("student", student);
        model.addAttribute("enrollments", enrollments);

        return "student-dashboard";
    }

    

    
}
