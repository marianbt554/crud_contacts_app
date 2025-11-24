package com.marian_bt.contacts_app.repository;

import com.marian_bt.contacts_app.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

import java.util.List;

public interface ContactRepository extends JpaRepository <Contact, Long> {


    @Query("""
    SELECT c
    FROM Contact c
    WHERE (:firstName     IS NULL OR LOWER(c.firstName)   LIKE LOWER(CONCAT('%', :firstName, '%')))
      AND (:lastName      IS NULL OR LOWER(c.lastName)    LIKE LOWER(CONCAT('%', :lastName, '%')))
      AND (:institution   IS NULL OR LOWER(c.institution) LIKE LOWER(CONCAT('%', :institution, '%')))
      AND (:email         IS NULL OR LOWER(c.email)       LIKE LOWER(CONCAT('%', :email, '%')))
      AND (:persGroup     IS NULL OR LOWER(c.persGroup)   LIKE LOWER(CONCAT('%', :persGroup, '%')))
      AND (:country       IS NULL OR LOWER(c.country)     LIKE LOWER(CONCAT('%', :country, '%')))
      AND (:fundUse       IS NULL OR LOWER(c.fundUse)     LIKE LOWER(CONCAT('%', :fundUse, '%')))
      AND (:postAddress   IS NULL OR LOWER(c.postAddress) LIKE LOWER(CONCAT('%', :postAddress, '%')))
      AND (:phone1        IS NULL OR LOWER(c.phone1)      LIKE LOWER(CONCAT('%', :phone1, '%')))
      AND (:phone2        IS NULL OR LOWER(c.phone2)      LIKE LOWER(CONCAT('%', :phone2, '%')))
      AND (:faculty       IS NULL OR LOWER(c.faculty)     LIKE LOWER(CONCAT('%', :faculty, '%')))
      AND (:studyDomain   IS NULL OR LOWER(c.studyDomain) LIKE LOWER(CONCAT('%', :studyDomain, '%')))
      AND (:gender        IS NULL OR LOWER(c.gender)      LIKE LOWER(CONCAT('%', :gender, '%')))
      AND (:coilExp       IS NULL OR c.coilExp            = :coilExp)
      AND (:mobilityFin   IS NULL OR c.mobilityFin        = :mobilityFin)
      AND (:createdAfter  IS NULL OR c.createdAt          >= :createdAfter)
      AND (:createdBefore IS NULL OR c.createdAt          <= :createdBefore)
      AND (:updatedAfter  IS NULL OR c.updatedAt          >= :updatedAfter)
      AND (:updatedBefore IS NULL OR c.updatedAt          <= :updatedBefore)
    ORDER BY c.lastName ASC, c.firstName ASC, c.id ASC      
    """)
    List<Contact> searchContacts(
            @Param("firstName")     String firstName,
            @Param("lastName")      String lastName,
            @Param("institution")   String institution,
            @Param("email")         String email,
            @Param("persGroup")     String persGroup,
            @Param("country")       String country,
            @Param("fundUse")       String fundUse,
            @Param("postAddress")   String postAddress,
            @Param("phone1")        String phone1,
            @Param("phone2")        String phone2,
            @Param("faculty")       String faculty,
            @Param("studyDomain")   String studyDomain,
            @Param("gender")        String gender,
            @Param("coilExp")       Boolean coilExp,
            @Param("mobilityFin")   Boolean mobilityFin,
            @Param("createdAfter")  LocalDateTime createdAfter,
            @Param("createdBefore") LocalDateTime createdBefore,
            @Param("updatedAfter")  LocalDateTime updatedAfter,
            @Param("updatedBefore") LocalDateTime updatedBefore
    );



}