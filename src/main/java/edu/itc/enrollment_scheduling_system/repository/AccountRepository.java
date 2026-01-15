package edu.itc.enrollment_scheduling_system.repository;

import edu.itc.enrollment_scheduling_system.model.Account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    Optional<Account> findByEmail(String email);
}