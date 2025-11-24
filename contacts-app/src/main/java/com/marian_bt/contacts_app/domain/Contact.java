package com.marian_bt.contacts_app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;



@Entity
@Table(
        name = "contacts",
        indexes = {
             @Index(name = "idx_contacts_last_name", columnList = "lastName"),
             @Index(name = "idx_contacts_institution", columnList = "institution"),
             @Index(name = "idx_contacts_email", columnList = "email")
        }
)
public class Contact  {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long             id;


    private String           title;
    @NotBlank
    @Column(nullable = false)
    private String           firstName;
    @NotBlank
    @Column(nullable = false)
    private String           lastName;
    private String           persGroup;
    private String           function;
    @NotBlank
    @Column(nullable = false)
    private String           institution;
    private String           faculty;
    private String           studyDomain;
    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String           email;
    private String           phone1;
    private String           phone2;
    private String           postAddress;
    private String           country;
    private String           interest;
    private boolean          coilExp;
    private boolean          mobilityFin;
    private String           fundUse;
    private String           pastEvent;
    private String           contactPerson;
    @NotBlank
    @Column(nullable = false)
    @Pattern(
            regexp = "^(?i)(male|female|diverse)$",
            message = "Gender must be one of : male, female, diverse."
    )
    private String gender;
    private String           comments;
    private LocalDateTime    createdAt;
    private LocalDateTime    updatedAt;
    private String           createdBy; //reference to username later
    private String           updatedBy; //reference to username later


    public Contact() {

    }

    public Contact(String firstName,
                   String lastName,
                   String institution,
                   String email,
                   String gender){
        this.firstName =   firstName;
        this.lastName =    lastName;
        this.institution = institution;
        this.email =       email;
        this.gender =      gender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPersGroup() {
        return persGroup;
    }

    public void setPersGroup(String persGroup) {
        this.persGroup = persGroup;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getStudyDomain() {
        return studyDomain;
    }

    public void setStudyDomain(String studyDomain) {
        this.studyDomain = studyDomain;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPostAddress() {
        return postAddress;
    }

    public void setPostAddress(String postAddress) {
        this.postAddress = postAddress;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public boolean isCoilExp() {
        return coilExp;
    }

    public void setCoilExp(boolean coilExp) {
        this.coilExp = coilExp;
    }

    public boolean isMobilityFin() {
        return mobilityFin;
    }

    public void setMobilityFin(boolean mobilityFin) {
        this.mobilityFin = mobilityFin;
    }

    public String getFundUse() {
        return fundUse;
    }

    public void setFundUse(String fundUse) {
        this.fundUse = fundUse;
    }

    public String getPastEvent() {
        return pastEvent;
    }

    public void setPastEvent(String pastEvent) {
        this.pastEvent = pastEvent;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getGender() {
       return gender;
    }

    public void setGender(String gender) {
        if (gender == null) {
            this.gender = null;
        } else {
            this.gender = gender.trim().toLowerCase();
        }
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString(){
        return "Contact{"+
                "id=" + id +
                ", firstName =' " +    firstName + '\''+
                ", lastName = ' " +    lastName + '\'' +
                ", institution = ' " + institution + '\'' +
                ", email = ' " +       email + '\'' +
                ", gender = ' " +      gender + '\'' +
                '}';
    }
}
