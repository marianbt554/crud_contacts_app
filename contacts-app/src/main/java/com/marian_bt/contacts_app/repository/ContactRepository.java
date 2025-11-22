package com.marian_bt.contacts_app.repository;

import com.marian_bt.contacts_app.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

import java.util.List;

public interface ContactRepository extends JpaRepository <Contact, Long> {

    List<Contact> findAllByFirstNameContainingIgnoreCase(String firstName);
    List<Contact> findAllByLastNameContainingIgnoreCase(String lastName);
    List<Contact> findAllByEmailContainingIgnoreCase(String email);
    List<Contact> findAllByInstitutionContainingIgnoreCase(String institution);
    List<Contact> findAllByCreatedAtAfter(LocalDateTime createdAt);
    List<Contact> findAllByCreatedAtBefore(LocalDateTime createdAt);
    List<Contact> findAllByCreatedAtBetween(LocalDateTime createdAtAfter, LocalDateTime createdAtBefore);
    List<Contact> findAllByFunctionContainingIgnoreCase(String function);
    List<Contact> findAllByCountryContainingIgnoreCase(String country);

    @Query("""
        SELECT c
        FROM Contact c
        WHERE (:firstName IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')))
          AND (:lastName IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%')))
          AND (:institution IS NULL OR LOWER(c.institution) LIKE LOWER(CONCAT('%', :institution, '%')))
          AND (:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%')))
          AND (:persGroup IS NULL OR LOWER(c.persGroup) LIKE LOWER(CONCAT('%', :persGroup, '%')))
          AND (:country IS NULL OR LOWER(c.country) LIKE LOWER(CONCAT('%', :country, '%')))
          AND (:coilExp IS NULL OR c.coilExp = :coilExp)
          AND (:mobilityFin IS NULL OR c.mobilityFin = :mobilityFin)
          AND (:createdAfter IS NULL OR c.createdAt >= :createdAfter)
          AND (:createdBefore IS NULL OR c.createdAt <= :createdBefore)
        """)
    List<Contact> searchContacts(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("institution") String institution,
            @Param("email") String email,
            @Param("persGroup") String persGroup,
            @Param("country") String country,
            @Param("coilExp") Boolean coilExp,
            @Param("mobilityFin") Boolean mobilityFin,
            @Param("createdAfter") LocalDateTime createdAfter,
            @Param("createdBefore") LocalDateTime createdBefore
    );


}