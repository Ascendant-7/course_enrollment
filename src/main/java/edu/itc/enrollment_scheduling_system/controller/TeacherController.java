package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.model.Account;
import edu.itc.enrollment_scheduling_system.model.Classroom;
import edu.itc.enrollment_scheduling_system.repository.ClassroomRepository;
import edu.itc.enrollment_scheduling_system.security.AccountDetails;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final ClassroomRepository classroomRepository;

    @GetMapping("/dashboard")
    public String dashboard(
        Model model,
        @AuthenticationPrincipal AccountDetails accountDetails,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Account teacher = accountDetails.getAccount();

        Page<Classroom> classroomsPage = 
        classroomRepository.search(
            teacher,
             null,
            PageRequest.of(page, size)
        );

        model.addAttribute("teacher", teacher);
        model.addAttribute("classrooms", classroomsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", classroomsPage.getTotalPages());

        return "teacher-dashboard";
    }
}