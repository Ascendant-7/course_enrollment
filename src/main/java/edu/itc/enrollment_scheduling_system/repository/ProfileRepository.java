package edu.itc.enrollment_scheduling_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.itc.enrollment_scheduling_system.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long>{
    
}
