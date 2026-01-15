package edu.itc.enrollment_scheduling_system.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import edu.itc.enrollment_scheduling_system.dto.RegistrationDTO;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull private String username;
    @NonNull private String email;
    @NonNull private String passwordHash;
    
    @Column(nullable = false)
    private Boolean enabled;
    @Column(nullable = false)
    private Boolean approved;
    @Column(nullable = false)
    private Boolean accountNonExpired;
    @Column(nullable = false)
    private Boolean credentialsNonExpired;
    @Column(nullable = false)
    private Boolean accountNonLocked;

    @Column(updatable = false, insertable = false)
    private LocalDateTime createdAt;
    @Column(updatable = false, insertable = false)
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public Account(RegistrationDTO form, String passwordHash) {
        this.username = form.username();
        this.email = form.email();
        this.passwordHash = passwordHash;
        this.profile = new Profile(form.firstName(), form.lastName(), this);
    }

    @Transient
    public Boolean isActive() {
        return enabled && approved;
    }
}
