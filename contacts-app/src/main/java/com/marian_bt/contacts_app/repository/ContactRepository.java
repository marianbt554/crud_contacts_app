package com.marian_bt.contacts_app.repository;

import com.marian_bt.contacts_app.domain.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    Optional<Contact> findByEmailIgnoreCase(String email);

    @Query("""
    SELECT c
    FROM Contact c
    WHERE (:title              IS NULL OR LOWER(c.title)             LIKE LOWER(CONCAT('%', :title, '%')))
      AND (:firstName           IS NULL OR LOWER(c.firstName)         LIKE LOWER(CONCAT('%', :firstName, '%')))
      AND (:lastName            IS NULL OR LOWER(c.lastName)          LIKE LOWER(CONCAT('%', :lastName, '%')))
      AND (:gender              IS NULL OR LOWER(c.gender)            LIKE LOWER(CONCAT('%', :gender, '%')))
      AND (:email               IS NULL OR LOWER(c.email)             LIKE LOWER(CONCAT('%', :email, '%')))
      AND (:phone1              IS NULL OR LOWER(c.phone1)            LIKE LOWER(CONCAT('%', :phone1, '%')))
      AND (:phone2              IS NULL OR LOWER(c.phone2)            LIKE LOWER(CONCAT('%', :phone2, '%')))
      AND (:postAddress         IS NULL OR LOWER(c.postAddress)       LIKE LOWER(CONCAT('%', :postAddress, '%')))
      AND (:institution         IS NULL OR LOWER(c.institution)       LIKE LOWER(CONCAT('%', :institution, '%')))
      AND (:faculty             IS NULL OR LOWER(c.faculty)           LIKE LOWER(CONCAT('%', :faculty, '%')))
      AND (:studyDomain         IS NULL OR LOWER(c.studyDomain)       LIKE LOWER(CONCAT('%', :studyDomain, '%')))
      AND (:persGroup           IS NULL OR LOWER(c.persGroup)         LIKE LOWER(CONCAT('%', :persGroup, '%')))
      AND (:function            IS NULL OR LOWER(c.function)          LIKE LOWER(CONCAT('%', :function, '%')))
      AND (:country             IS NULL OR LOWER(c.country)           LIKE LOWER(CONCAT('%', :country, '%')))
      AND (:interest            IS NULL OR LOWER(c.interest)          LIKE LOWER(CONCAT('%', :interest, '%')))
      AND (:fundUse             IS NULL OR LOWER(c.fundUse)           LIKE LOWER(CONCAT('%', :fundUse, '%')))
      AND (:pastEvent           IS NULL OR LOWER(c.pastEvent)         LIKE LOWER(CONCAT('%', :pastEvent, '%')))
      AND (:comments            IS NULL OR LOWER(c.comments)          LIKE LOWER(CONCAT('%', :comments, '%')))
      AND (:contactedByIngenium IS NULL OR c.contactedByIngenium      = :contactedByIngenium)
      AND (:coilExp             IS NULL OR c.coilExp                  = :coilExp)
      AND (:mobilityFin         IS NULL OR c.mobilityFin              = :mobilityFin)
      AND (:createdAfter        IS NULL OR c.createdAt                >= :createdAfter)
      AND (:createdBefore       IS NULL OR c.createdAt                <= :createdBefore)
      AND (:updatedAfter        IS NULL OR c.updatedAt                >= :updatedAfter)
      AND (:updatedBefore       IS NULL OR c.updatedAt                <= :updatedBefore)
    ORDER BY c.lastName ASC, c.firstName ASC, c.id ASC
    """)
    List<Contact> searchContacts(
            @Param("title")              String title,
            @Param("firstName")           String firstName,
            @Param("lastName")            String lastName,
            @Param("gender")              String gender,
            @Param("email")               String email,
            @Param("phone1")              String phone1,
            @Param("phone2")              String phone2,
            @Param("postAddress")         String postAddress,
            @Param("institution")         String institution,
            @Param("faculty")             String faculty,
            @Param("studyDomain")         String studyDomain,
            @Param("persGroup")           String persGroup,
            @Param("function")            String function,
            @Param("country")             String country,
            @Param("interest")            String interest,
            @Param("fundUse")             String fundUse,
            @Param("pastEvent")           String pastEvent,
            @Param("comments")            String comments,
            @Param("contactedByIngenium") Boolean contactedByIngenium,
            @Param("coilExp")             Boolean coilExp,
            @Param("mobilityFin")         Boolean mobilityFin,
            @Param("createdAfter")        LocalDateTime createdAfter,
            @Param("createdBefore")       LocalDateTime createdBefore,
            @Param("updatedAfter")        LocalDateTime updatedAfter,
            @Param("updatedBefore")       LocalDateTime updatedBefore
    );

    @Query(value = """
    SELECT c
    FROM Contact c
    WHERE (:title              IS NULL OR LOWER(c.title)             LIKE LOWER(CONCAT('%', :title, '%')))
      AND (:firstName           IS NULL OR LOWER(c.firstName)         LIKE LOWER(CONCAT('%', :firstName, '%')))
      AND (:lastName            IS NULL OR LOWER(c.lastName)          LIKE LOWER(CONCAT('%', :lastName, '%')))
      AND (:gender              IS NULL OR LOWER(c.gender)            LIKE LOWER(CONCAT('%', :gender, '%')))
      AND (:email               IS NULL OR LOWER(c.email)             LIKE LOWER(CONCAT('%', :email, '%')))
      AND (:phone1              IS NULL OR LOWER(c.phone1)            LIKE LOWER(CONCAT('%', :phone1, '%')))
      AND (:phone2              IS NULL OR LOWER(c.phone2)            LIKE LOWER(CONCAT('%', :phone2, '%')))
      AND (:postAddress         IS NULL OR LOWER(c.postAddress)       LIKE LOWER(CONCAT('%', :postAddress, '%')))
      AND (:institution         IS NULL OR LOWER(c.institution)       LIKE LOWER(CONCAT('%', :institution, '%')))
      AND (:faculty             IS NULL OR LOWER(c.faculty)           LIKE LOWER(CONCAT('%', :faculty, '%')))
      AND (:studyDomain         IS NULL OR LOWER(c.studyDomain)       LIKE LOWER(CONCAT('%', :studyDomain, '%')))
      AND (:persGroup           IS NULL OR LOWER(c.persGroup)         LIKE LOWER(CONCAT('%', :persGroup, '%')))
      AND (:function            IS NULL OR LOWER(c.function)          LIKE LOWER(CONCAT('%', :function, '%')))
      AND (:country             IS NULL OR LOWER(c.country)           LIKE LOWER(CONCAT('%', :country, '%')))
      AND (:interest            IS NULL OR LOWER(c.interest)          LIKE LOWER(CONCAT('%', :interest, '%')))
      AND (:fundUse             IS NULL OR LOWER(c.fundUse)           LIKE LOWER(CONCAT('%', :fundUse, '%')))
      AND (:pastEvent           IS NULL OR LOWER(c.pastEvent)         LIKE LOWER(CONCAT('%', :pastEvent, '%')))
      AND (:comments            IS NULL OR LOWER(c.comments)          LIKE LOWER(CONCAT('%', :comments, '%')))
      AND (:contactedByIngenium IS NULL OR c.contactedByIngenium      = :contactedByIngenium)
      AND (:coilExp             IS NULL OR c.coilExp                  = :coilExp)
      AND (:mobilityFin         IS NULL OR c.mobilityFin              = :mobilityFin)
      AND (:createdAfter        IS NULL OR c.createdAt                >= :createdAfter)
      AND (:createdBefore       IS NULL OR c.createdAt                <= :createdBefore)
      AND (:updatedAfter        IS NULL OR c.updatedAt                >= :updatedAfter)
      AND (:updatedBefore       IS NULL OR c.updatedAt                <= :updatedBefore)
    """,
            countQuery = """
    SELECT COUNT(c)
    FROM Contact c
    WHERE (:title              IS NULL OR LOWER(c.title)             LIKE LOWER(CONCAT('%', :title, '%')))
      AND (:firstName           IS NULL OR LOWER(c.firstName)         LIKE LOWER(CONCAT('%', :firstName, '%')))
      AND (:lastName            IS NULL OR LOWER(c.lastName)          LIKE LOWER(CONCAT('%', :lastName, '%')))
      AND (:gender              IS NULL OR LOWER(c.gender)            LIKE LOWER(CONCAT('%', :gender, '%')))
      AND (:email               IS NULL OR LOWER(c.email)             LIKE LOWER(CONCAT('%', :email, '%')))
      AND (:phone1              IS NULL OR LOWER(c.phone1)            LIKE LOWER(CONCAT('%', :phone1, '%')))
      AND (:phone2              IS NULL OR LOWER(c.phone2)            LIKE LOWER(CONCAT('%', :phone2, '%')))
      AND (:postAddress         IS NULL OR LOWER(c.postAddress)       LIKE LOWER(CONCAT('%', :postAddress, '%')))
      AND (:institution         IS NULL OR LOWER(c.institution)       LIKE LOWER(CONCAT('%', :institution, '%')))
      AND (:faculty             IS NULL OR LOWER(c.faculty)           LIKE LOWER(CONCAT('%', :faculty, '%')))
      AND (:studyDomain         IS NULL OR LOWER(c.studyDomain)       LIKE LOWER(CONCAT('%', :studyDomain, '%')))
      AND (:persGroup           IS NULL OR LOWER(c.persGroup)         LIKE LOWER(CONCAT('%', :persGroup, '%')))
      AND (:function            IS NULL OR LOWER(c.function)          LIKE LOWER(CONCAT('%', :function, '%')))
      AND (:country             IS NULL OR LOWER(c.country)           LIKE LOWER(CONCAT('%', :country, '%')))
      AND (:interest            IS NULL OR LOWER(c.interest)          LIKE LOWER(CONCAT('%', :interest, '%')))
      AND (:fundUse             IS NULL OR LOWER(c.fundUse)           LIKE LOWER(CONCAT('%', :fundUse, '%')))
      AND (:pastEvent           IS NULL OR LOWER(c.pastEvent)         LIKE LOWER(CONCAT('%', :pastEvent, '%')))
      AND (:comments            IS NULL OR LOWER(c.comments)          LIKE LOWER(CONCAT('%', :comments, '%')))
      AND (:contactedByIngenium IS NULL OR c.contactedByIngenium      = :contactedByIngenium)
      AND (:coilExp             IS NULL OR c.coilExp                  = :coilExp)
      AND (:mobilityFin         IS NULL OR c.mobilityFin              = :mobilityFin)
      AND (:createdAfter        IS NULL OR c.createdAt                >= :createdAfter)
      AND (:createdBefore       IS NULL OR c.createdAt                <= :createdBefore)
      AND (:updatedAfter        IS NULL OR c.updatedAt                >= :updatedAfter)
      AND (:updatedBefore       IS NULL OR c.updatedAt                <= :updatedBefore)
    """)
    Page<Contact> searchContactsPaged(
            @Param("title")              String title,
            @Param("firstName")           String firstName,
            @Param("lastName")            String lastName,
            @Param("gender")              String gender,
            @Param("email")               String email,
            @Param("phone1")              String phone1,
            @Param("phone2")              String phone2,
            @Param("postAddress")         String postAddress,
            @Param("institution")         String institution,
            @Param("faculty")             String faculty,
            @Param("studyDomain")         String studyDomain,
            @Param("persGroup")           String persGroup,
            @Param("function")            String function,
            @Param("country")             String country,
            @Param("interest")            String interest,
            @Param("fundUse")             String fundUse,
            @Param("pastEvent")           String pastEvent,
            @Param("comments")            String comments,
            @Param("contactedByIngenium") Boolean contactedByIngenium,
            @Param("coilExp")             Boolean coilExp,
            @Param("mobilityFin")         Boolean mobilityFin,
            @Param("createdAfter")        LocalDateTime createdAfter,
            @Param("createdBefore")       LocalDateTime createdBefore,
            @Param("updatedAfter")        LocalDateTime updatedAfter,
            @Param("updatedBefore")       LocalDateTime updatedBefore,
            Pageable pageable
    );
}
