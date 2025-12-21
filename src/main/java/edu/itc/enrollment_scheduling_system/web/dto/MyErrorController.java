package edu.itc.enrollment_scheduling_system.web.dto;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == 404) {
                return "error/404";
            } else if (statusCode == 403) {
                return "error/403";
            } else if (statusCode == 500) {
                return "error/500";
            }
        }
        return "error/500"; 
    }

    // បន្ថែមសម្រាប់តេស្តមើល UI ផ្ទាល់
    @RequestMapping("/test403")
    public String test403() {
        return "error/403"; 
    }

    @RequestMapping("/test500")
    public String test500() {
        return "error/500"; 
    }
}