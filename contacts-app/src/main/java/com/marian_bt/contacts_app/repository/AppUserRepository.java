package com.marian_bt.contacts_app.repository;

import com.marian_bt.contacts_app.domain.AppUser;
import com.marian_bt.contacts_app.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    long countByRole(UserRole role);
}
