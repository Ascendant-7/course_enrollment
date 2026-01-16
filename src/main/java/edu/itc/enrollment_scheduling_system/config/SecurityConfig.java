package edu.itc.enrollment_scheduling_system.config;

import edu.itc.enrollment_scheduling_system.service.AccountDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AccountDetailsService userDetailsService;
    private final RoleBasedSuccessHandler successHandler;

    public SecurityConfig(AccountDetailsService userDetailsService, 
                         RoleBasedSuccessHandler successHandler) {
        this.userDetailsService = userDetailsService;
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception
    {
        http
            .authorizeHttpRequests(auth -> auth
                // Public pages
                .requestMatchers(
                    "/",
                    "/home",
                    "/index",
                    "/login",
                    "/register",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/h2-console/**",
                    "/error/**",
                    "/courses",
                    "/courses/**"
                ).permitAll()

                // Admin only
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // Teacher only
                .requestMatchers("/teacher/**").hasRole("TEACHER")
                
                // Student only
                .requestMatchers("/student/**").hasRole("STUDENT")
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(successHandler)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .userDetailsService(userDetailsService)
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}