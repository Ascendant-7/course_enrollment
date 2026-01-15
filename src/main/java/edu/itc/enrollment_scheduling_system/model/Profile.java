package edu.itc.enrollment_scheduling_system.model;

import java.util.Objects;

import edu.itc.enrollment_scheduling_system.dto.UserProfileForm;
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

    public Profile(String firstName, String lastName, Account account) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.account = account;
    }

    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public void update(UserProfileForm form) {
        if (!Objects.equals(this.firstName, form.firstName())) this.firstName = form.firstName();
        if (!Objects.equals(this.lastName, form.lastName())) this.lastName = form.lastName();
        if (!Objects.equals(this.bio, form.bio())) this.bio = form.bio();
        if (!Objects.equals(this.phone, form.phone())) this.phone = form.phone();
        if (!Objects.equals(this.address, form.address())) this.address = form.address();
    }
}
