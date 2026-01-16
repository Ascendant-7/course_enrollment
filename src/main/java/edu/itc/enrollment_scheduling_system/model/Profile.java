package edu.itc.enrollment_scheduling_system.model;

import java.util.Objects;

import edu.itc.enrollment_scheduling_system.dto.UpdateProfileDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class Profile {

    @Id
    private Long accountId;

    @NonNull private String firstName;
    @NonNull private String lastName;
    private String bio;
    private String phone;
    private String address;
    
    @NonNull
    @OneToOne
    @MapsId
    @JoinColumn(name = "account_id")
    private Account account;

    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public void update(UpdateProfileDTO form) {
        if (!Objects.equals(this.firstName, form.firstName())) this.firstName = form.firstName();
        if (!Objects.equals(this.lastName, form.lastName())) this.lastName = form.lastName();
        if (!Objects.equals(this.bio, form.bio())) this.bio = form.bio();
        if (!Objects.equals(this.phone, form.phone())) this.phone = form.phone();
        if (!Objects.equals(this.address, form.address())) this.address = form.address();
    }
}
