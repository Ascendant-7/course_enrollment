package edu.itc.enrollment_scheduling_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NonNull;

@Entity
@Table(name = "profiles")
@Data
public class Profile {

    @Id
    private Long account_id;

    @NonNull private String firstName;
    @NonNull private String lastName;
    private String bio;
    private String phone;
    private String address;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Account account;

    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
