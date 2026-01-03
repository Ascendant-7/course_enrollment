package edu.itc.enrollment_scheduling_system.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // store BCrypt hash

    @Column(nullable = false)
    private boolean enabled = true;

    // Profile fields
    @Column(name = "full_name")
    private String fullName; // read-only from profile page per requirements

    @Column
    private String email;

    @Column(length = 1000)
    private String bio;

    @Column
    private String phone;

    @Column
    private String address;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User() {}
}
