package edu.itc.enrollment_scheduling_system.service;

import edu.itc.enrollment_scheduling_system.model.Account;
import edu.itc.enrollment_scheduling_system.repository.AccountRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class DbUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public DbUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Account account = accountRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new org.springframework.security.core.userdetails.User(
            account.getUsername(),
            account.getPasswordHash(),
            account.isActive(),
            account.getAccountNonExpired(),
            account.getCredentialsNonExpired(),
            account.getAccountNonLocked(),
            getAuthorities(account)
            
        );
    }

    public Collection<? extends GrantedAuthority> getAuthorities(Account account) {
        return Optional.ofNullable(account.getRoles())
            .orElse(Collections.emptySet())
            .stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .toList();
    }
}