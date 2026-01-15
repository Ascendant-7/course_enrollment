package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.dto.PasswordChangeForm;
import edu.itc.enrollment_scheduling_system.dto.UserProfileForm;
import edu.itc.enrollment_scheduling_system.model.Account;
import edu.itc.enrollment_scheduling_system.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;

    @GetMapping("/")
    public String profile(
        @AuthenticationPrincipal Account account,
        Model model
    ) {
        model.addAttribute(
            "form",
            new UserProfileForm(account.getProfile())
        );
        return "profile";
    }

    @GetMapping("/change-password")
    public String getChangePasswordForm(
        @AuthenticationPrincipal Account account,
        @ModelAttribute PasswordChangeForm form
    ) { return "change-password"; }

    @PostMapping("/change-password")
    public String changePassword(
        @Valid @ModelAttribute PasswordChangeForm form,
        BindingResult result,
        @AuthenticationPrincipal Account account,
        RedirectAttributes redirection
    ) {
        if (result.hasErrors()) return "change-password";

        userService.changePassword(form, account, result);
        redirection.addFlashAttribute(
            "successMessage",
            "Password changed successfully!"
        );
        return "redirect:/profile";
    }

    @GetMapping("/edit")
    public String showEditForm(
        @AuthenticationPrincipal Account account,
        Model model
    ) {
        model.addAttribute(
            "form",
            new UserProfileForm(account.getProfile())
        );
        return "profile-edit";
    }

    @PostMapping("/edit")
    public String updateProfile(
        @AuthenticationPrincipal Account account,
        @Valid @ModelAttribute("form") UserProfileForm form,
        BindingResult bindingResult,
        Model model,
        RedirectAttributes redirection
    ) {
        if (bindingResult.hasErrors()) {
            return "profile-edit";
        }

        account.getProfile().update(form);
        redirection.addFlashAttribute(
            "successMessage",
            "Profile changed successfully!"
        );
        return "redirect:/profile";
    }
}
