package edu.itc.enrollment_scheduling_system.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@DynamicInsert
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull private String username;
    @NonNull private String email;
    @NonNull private String passwordHash;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    private Boolean enabled;
    private Boolean approved;
    private Boolean accountNonExpired;
    private Boolean credentialsNonExpired;
    private Boolean accountNonLocked;

    @Column(updatable = false, insertable = false)
    private LocalDateTime createdAt;
    @Column(updatable = false, insertable = false)
    private LocalDateTime updatedAt;

    @OneToOne(
        mappedBy = "account",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Profile profile;

    @Transient
    public Boolean isActive() {
        return Boolean.TRUE.equals(enabled) && Boolean.TRUE.equals(approved);
    }

    public void grant(Role role) {
        this.approved = true;
        this.role = role;
    }
}
