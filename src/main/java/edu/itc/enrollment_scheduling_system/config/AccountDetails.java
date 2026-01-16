package edu.itc.enrollment_scheduling_system.config;

import java.util.Collection;
import java.util.List;

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
        // Spring Security expects ROLE_ prefix for authority names
        return List.of(new SimpleGrantedAuthority("ROLE_" + account.getRole().name()));
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
        Boolean value = account.getAccountNonExpired();
        if (value == null) {
            throw new IllegalStateException("Account " + account.getId() + " missing accountNonExpired default");
        }
        return value;
    }

    @Override
    public boolean isAccountNonLocked() {
        Boolean value = account.getAccountNonLocked();
        if (value == null) {
            throw new IllegalStateException("Account " + account.getId() + " missing accountNonLocked default");
        }
        return value;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        Boolean value = account.getCredentialsNonExpired();
        if (value == null) {
            throw new IllegalStateException("Account " + account.getId() + " missing credentialsNonExpired default");
        }
        return value;
    }

    @Override
    public boolean isEnabled() {
        Boolean enabled = account.getEnabled();
        Boolean approved = account.getApproved();
        if (enabled == null) {
            throw new IllegalStateException("Account " + account.getId() + " missing enabled default");
        }
        if (approved == null) {
            throw new IllegalStateException("Account " + account.getId() + " missing approved default");
        }
        return enabled && approved;
    }

}
