package edu.itc.enrollment_scheduling_system.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Extract HTTP status code
        Object statusObj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int statusCode = statusObj != null ? Integer.parseInt(statusObj.toString()) : 500;

        // Extract error message if available
        Object messageObj = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        String message = messageObj != null ? messageObj.toString() : null;

        // Extract exception if available
        Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        String exceptionMessage = throwable != null ? throwable.getMessage() : null;

        // Use exception message if no explicit error message
        if (message == null && exceptionMessage != null) {
            message = exceptionMessage;
        }

        // Add attributes to model for template
        model.addAttribute("status", statusCode);
        model.addAttribute("message", message);

        // Return unified error template
        return "shared/error";
    }
}
