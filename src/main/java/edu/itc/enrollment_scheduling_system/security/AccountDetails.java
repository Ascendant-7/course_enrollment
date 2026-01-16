package edu.itc.enrollment_scheduling_system.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import edu.itc.enrollment_scheduling_system.model.Account;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountDetails implements UserDetails{

    private final Account account;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return account.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .toList();
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public String getPassword() {
        return account.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return account.getAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return account.getAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return account.getCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return account.isActive();
    }

}
