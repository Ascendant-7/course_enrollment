package edu.itc.enrollment_scheduling_system.controller;

import edu.itc.enrollment_scheduling_system.config.AccountDetails;
import edu.itc.enrollment_scheduling_system.dto.ChangePasswordDTO;
import edu.itc.enrollment_scheduling_system.dto.UpdateProfileDTO;
import edu.itc.enrollment_scheduling_system.service.AccountService;
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
    private final AccountService userService;

    @GetMapping("/")
    public String profile(
        @AuthenticationPrincipal AccountDetails accountDetails,
        Model model
    ) {
        model.addAttribute(
            "form",
            new UpdateProfileDTO(accountDetails.getAccount().getProfile())
        );
        return "user/profile";
    }

    @GetMapping("/change-password")
    public String getChangePasswordForm(
        @AuthenticationPrincipal AccountDetails accountDetails,
        @ModelAttribute ChangePasswordDTO form
    ) { return "user/change-password"; }

    @PostMapping("/change-password")
    public String changePassword(
        @Valid @ModelAttribute ChangePasswordDTO form,
        BindingResult result,
        @AuthenticationPrincipal AccountDetails accountDetails,
        RedirectAttributes redirection
    ) {
        if (result.hasErrors()) return "user/change-password";

        userService.changePassword(form, accountDetails.getAccount(), result);
        redirection.addFlashAttribute(
            "successMessage",
            "Password changed successfully!"
        );
        return "redirect:/profile";
    }

    @GetMapping("/edit")
    public String showEditForm(
        @AuthenticationPrincipal AccountDetails accountDetails,
        Model model
    ) {
        model.addAttribute(
            "form",
            new UpdateProfileDTO(accountDetails.getAccount().getProfile())
        );
        return "user/edit";
    }

    @PostMapping("/edit")
    public String updateProfile(
        @AuthenticationPrincipal AccountDetails accountDetails,
        @Valid @ModelAttribute("form") UpdateProfileDTO form,
        BindingResult bindingResult,
        Model model,
        RedirectAttributes redirection
    ) {
        if (bindingResult.hasErrors()) {
            return "user/edit";
        }

        accountDetails.getAccount().getProfile().update(form);
        redirection.addFlashAttribute(
            "successMessage",
            "Profile changed successfully!"
        );
        return "redirect:/profile";
    }
}
