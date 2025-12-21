package edu.itc.enrollment_scheduling_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final RoleBasedSuccessHandler roleBasedSuccessHandler;

    public SecurityConfig(RoleBasedSuccessHandler roleBasedSuccessHandler) {
        this.roleBasedSuccessHandler = roleBasedSuccessHandler;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Dev-only H2 console support
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**", "/h2-console/**").permitAll()

                // Optional: restrict registration to admins (recommended in real apps)
                // .requestMatchers("/register").hasRole("ADMIN")

                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/teacher/**").hasRole("TEACHER")
                .requestMatchers("/student/**").hasRole("STUDENT")

                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(roleBasedSuccessHandler)
                .failureUrl("/login?error")
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .permitAll()
            )

            // Session management
            .sessionManagement(session -> session
                .sessionFixation(sf -> sf.migrateSession())
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            )

            // Stretch: Remember-me
            .rememberMe(rm -> rm
                .key("change-me-rememberme-key")
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(7 * 24 * 60 * 60) // 7 days
            );

        return http.build();
    }
}