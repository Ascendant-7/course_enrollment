package edu.itc.enrollment_scheduling_system.controller;

import lombok.RequiredArgsConstructor;

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

import edu.itc.enrollment_scheduling_system.config.AccountDetails;
import edu.itc.enrollment_scheduling_system.model.Account;
import edu.itc.enrollment_scheduling_system.model.Role;
import edu.itc.enrollment_scheduling_system.repository.AccountRepository;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AccountRepository accountRepository;

    @GetMapping("/dashboard")
    public String dashboard(
        Model model,
        @AuthenticationPrincipal AccountDetails accountDetails,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Page<Account> pendingUsers = accountRepository.findByApprovedFalse(
            PageRequest.of(page, size)
        );

        model.addAttribute("pendingUsers", pendingUsers.getContent());
        model.addAttribute("pendingUserCount", pendingUsers.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pendingUsers.getTotalPages());
        model.addAttribute("allRoles", Role.values());
            return "admin/dashboard";
    }

    @PostMapping("/approvals/{id}/approve")
    public String approveUser(
        @PathVariable @NonNull Long id,
        @RequestParam("role") @NonNull Role role,
        RedirectAttributes redirectAttributes
    ) {
        Account account = accountRepository.findById(id).orElse(null);

        if (account == null) {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/admin/dashboard";
        }

        account.grant(role);
        accountRepository.save(account);

        redirectAttributes.addFlashAttribute(
            "success",
            "User '" + account.getUsername() + "' approved successfully as " + role.toString() + "!"
        );

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
        accountRepository.delete(user);  // Cascades delete to Profile

        redirectAttributes.addFlashAttribute("success", "User '" + username + "' registration denied and removed");

        return "redirect:/admin/dashboard";
    }
}
