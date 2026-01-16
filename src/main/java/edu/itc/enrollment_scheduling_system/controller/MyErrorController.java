package edu.itc.enrollment_scheduling_system.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class MyErrorController implements ErrorController {

    @RequestMapping
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Integer statusCode = status != null ? Integer.valueOf(status.toString()) : 500;
        model.addAttribute("status", statusCode);
        return "error";
    }
}