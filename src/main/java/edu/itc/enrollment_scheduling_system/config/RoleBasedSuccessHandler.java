package edu.itc.enrollment_scheduling_system.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class RoleBasedSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        System.out.println("=== LOGIN SUCCESS ===");
        System.out.println("Username: " + authentication.getName());
        System.out.println("Authorities: " + authorities);

        String redirectUrl = "/";

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            System.out.println("Checking role: " + role);

            if (role.equals("ROLE_ADMIN")) {
                redirectUrl = "/admin/dashboard";
                System.out.println("Redirecting admin to: " + redirectUrl);
                break;
            } else if (role.equals("ROLE_TEACHER")) {
                redirectUrl = "/teacher/dashboard";
                System.out.println("Redirecting teacher to: " + redirectUrl);
                break;
            } else if (role.equals("ROLE_STUDENT")) {
                redirectUrl = "/student/dashboard";
                System.out.println("Redirecting student to: " + redirectUrl);
                break;
            }
        }

        System.out.println("Final redirect URL: " + redirectUrl);
        System.out.println("===================");

        response.sendRedirect(redirectUrl);
    }
}