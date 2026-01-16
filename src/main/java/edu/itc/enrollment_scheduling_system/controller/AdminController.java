package edu.itc.enrollment_scheduling_system.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.itc.enrollment_scheduling_system.model.Account;
import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.model.Role;
import edu.itc.enrollment_scheduling_system.repository.AccountRepository;
import edu.itc.enrollment_scheduling_system.repository.CourseRepository;
import edu.itc.enrollment_scheduling_system.repository.RoleRepository;
import edu.itc.enrollment_scheduling_system.security.AccountDetails;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AccountRepository accountRepository;
    private final CourseRepository courseRepository;
    private final RoleRepository roleRepository;

    @GetMapping("/dashboard")
    public String dashboard(
        Model model,
        @AuthenticationPrincipal AccountDetails accountDetails,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Page<Account> pendingUsersPage = accountRepository.findByApprovedFalse(
            PageRequest.of(page, size)
        );

        model.addAttribute("pendingUsers", pendingUsersPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pendingUsersPage.getTotalPages());

        return "admin-dashboard";
    }

    @PostMapping("/approvals/{id}/approve")
    public String approveUser(
        @PathVariable @NonNull Long id,
        @RequestParam("roleId") @NonNull Long roleId,
        @RequestParam(value = "courseIds", required = false) List<Long> courseIds,
        RedirectAttributes redirectAttributes
    ) {
        Account user = accountRepository.findById(id).orElse(null);
        Role role = roleRepository.findById(roleId).orElse(null);

        if (user == null || role == null) {
            redirectAttributes.addFlashAttribute("error", "User or role not found");
            return "redirect:/admin/dashboard";
        }

        // Approve user and assign role
        user.setApproved(true);
        user.getRoles().clear();
        user.getRoles().add(role);

        // If teacher role and courses selected, assign them
        if ("ROLE_TEACHER".equals(role.getName()) && courseIds != null && !courseIds.isEmpty()) {
            for (Long courseId : courseIds) {
                Long nonNullCourseId = Objects.requireNonNull(courseId, "courseId must not be null");
                Course course = courseRepository.findById(nonNullCourseId).orElse(null);
                if (course != null) {
                    course.setTeacher(user);
                    courseRepository.save(course);
                }
            }
        }

        accountRepository.save(user);

        redirectAttributes.addFlashAttribute("success", 
            "User " + user.getUsername() + " approved successfully as " + role.getName());

        return "redirect:/admin/dashboard";
    }

    @PostMapping("/approvals/{id}/deny")
    public String denyUser(
        @PathVariable @NonNull Long id,
        RedirectAttributes redirectAttributes
    ) {
        Account user = accountRepository.findById(id).orElse(null);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/admin/dashboard";
        }

        String username = user.getUsername();
        accountRepository.delete(user);

        redirectAttributes.addFlashAttribute("success", "User " + username + " registration denied and removed");

        return "redirect:/admin/dashboard";
    }
}
