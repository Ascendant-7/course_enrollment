package edu.itc.enrollment_scheduling_system.repository;

import edu.itc.enrollment_scheduling_system.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Integer> {
}