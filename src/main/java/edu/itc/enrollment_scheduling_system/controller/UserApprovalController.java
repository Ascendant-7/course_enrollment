package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.model.Course;
import edu.itc.enrollment_scheduling_system.model.Role;
import edu.itc.enrollment_scheduling_system.model.Account;
import edu.itc.enrollment_scheduling_system.repository.CourseRepository;
import edu.itc.enrollment_scheduling_system.repository.RoleRepository;
import edu.itc.enrollment_scheduling_system.repository.AccountRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin/approvals")
public class UserApprovalController {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final CourseRepository courseRepository;

    public UserApprovalController(AccountRepository accountRepository, RoleRepository roleRepository, CourseRepository courseRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public String listPendingApprovals(Model model) {
        List<Account> pendingUsers = accountRepository.findByApprovedFalse();
        model.addAttribute("pendingUsers", pendingUsers);
        return "admin-approvals";
    }

    @GetMapping("/{id}")
    public String reviewUser(@PathVariable @NonNull Long id, Model model) {
        Account user = accountRepository.findById(id).orElse(null);
        
        if (user == null || user.isApproved()) {
            return "redirect:/admin/approvals";
        }

        List<Role> roles = roleRepository.findAll();
        List<Course> courses = courseRepository.findAll();

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("courses", courses);

        return "admin-user-review";
    }

    @PostMapping("/{id}/approve")
    public String approveUser(
            @PathVariable @NonNull Long id,
            @RequestParam("roleId") @NonNull Long roleId,
            @RequestParam(value = "courseIds", required = false) List<Long> courseIds,
            RedirectAttributes redirectAttributes) {

        Account user = accountRepository.findById(id).orElse(null);
        Role role = roleRepository.findById(roleId).orElse(null);

        if (user == null || role == null) {
            redirectAttributes.addFlashAttribute("error", "User or role not found");
            return "redirect:/admin/approvals";
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

        return "redirect:/admin/approvals";
    }

    @PostMapping("/{id}/deny")
    public String denyUser(@PathVariable @NonNull Long id, RedirectAttributes redirectAttributes) {
        Account user = accountRepository.findById(id).orElse(null);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/admin/approvals";
        }

        String username = user.getUsername();
        accountRepository.delete(user);

        redirectAttributes.addFlashAttribute("success", "User " + username + " registration denied and removed");

        return "redirect:/admin/approvals";
    }
}