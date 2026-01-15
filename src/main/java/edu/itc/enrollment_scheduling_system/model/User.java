package edu.itc.enrollment_scheduling_system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull private String username;
    @NonNull private String email;
    @NonNull private String firstName;
    @NonNull private String lastName;
    @NonNull private String passwordHash;

    private String bio;
    private String phone;
    private String address;
    
    @Column(nullable = false)
    private Boolean enabled;
    @Column(nullable = false)
    private Boolean approved;

    @Column(updatable = false, insertable = false)
    private LocalDateTime createdAt;
    @Column(updatable = false, insertable = false)
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    // No-arg constructor for JPA
    public User() {}

    // Constructor for required fields
    public User(String username, String email, String firstName, String lastName, String passwordHash) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.enabled = true;
        this.approved = false;
    }

    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Alias for passwordHash to match Spring Security expectations
    public String getPassword() {
        return passwordHash;
    }

    public void setPassword(String password) {
        this.passwordHash = password;
    }
}
