package com.marian_bt.contacts_app.service;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class ContactSearchCriteria {
    private String        firstName;
    private String        lastName;
    private String        institution;
    private String        email;
    private String        persGroup;
    private String        country;
    private Boolean       coilExp;
    private Boolean       mobilityFin;
    private String        fundUse;
    private String        postAddress;
    private String        phone1;
    private String        phone2;
    private String        faculty;
    private String        studyDomain;
    private String        gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd 'T' HH:mm")
    private LocalDateTime createdAfter;

    @DateTimeFormat(pattern = "yyyy-MM-dd 'T' HH:mm")
    private LocalDateTime createdBefore;

    @DateTimeFormat(pattern = "yyyy-MM-dd 'T' HH:mm")
    private LocalDateTime updatedAfter;
    @DateTimeFormat(pattern = "yyyy-MM-dd 'T' HH:mm")
    private LocalDateTime updatedBefore;

    private static String trimToNull(String value){
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
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

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPersGroup() {
        return persGroup;
    }

    public void setPersGroup(String persGroup) {
        this.persGroup = persGroup;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getCoilExp() {
        return coilExp;
    }

    public void setCoilExp(Boolean coilExp) {
        this.coilExp = coilExp;
    }

    public Boolean getMobilityFin() {
        return mobilityFin;
    }

    public void setMobilityFin(Boolean mobilityFin) {
        this.mobilityFin = mobilityFin;
    }

    public String getFundUse() {
        return fundUse;
    }

    public void setFundUse(String fundUse) {
        this.fundUse = fundUse;
    }

    public String getPostAddress() {
        return postAddress;
    }

    public void setPostAddress(String postAddress) {
        this.postAddress = postAddress;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDateTime getCreatedAfter() {
        return createdAfter;
    }

    public void setCreatedAfter(LocalDateTime createdAfter) {
        this.createdAfter = createdAfter;
    }

    public LocalDateTime getCreatedBefore() {
        return createdBefore;
    }

    public void setCreatedBefore(LocalDateTime createdBefore) {
        this.createdBefore = createdBefore;
    }

    public LocalDateTime getUpdatedAfter() {
        return updatedAfter;
    }

    public void setUpdatedAfter(LocalDateTime updatedAfter) {
        this.updatedAfter = updatedAfter;
    }

    public LocalDateTime getUpdatedBefore() {
        return updatedBefore;
    }

    public void setUpdatedBefore(LocalDateTime updatedBefore) {
        this.updatedBefore = updatedBefore;
    }

    private boolean isEmpty(){
        return firstName      == null &&
                lastName      == null &&
                institution   == null &&
                email         == null &&
                persGroup     == null &&
                country       == null &&
                coilExp       == null &&
                mobilityFin   == null &&
                fundUse       == null &&
                postAddress   == null &&
                phone1        == null &&
                phone2        == null &&
                faculty       == null &&
                studyDomain   == null &&
                gender        == null &&
                createdAfter  == null &&
                createdBefore == null &&
                updatedAfter  == null &&
                updatedBefore == null;

    }

    @Override
    public String toString() {
        return "ContactSearchCriteria{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", institution='" + institution + '\'' +
                ", email='" + email + '\'' +
                ", persGroup='" + persGroup + '\'' +
                ", country='" + country + '\'' +
                ", coilExp=" + coilExp +
                ", mobilityFin=" + mobilityFin +
                ", createdAfter=" + createdAfter +
                ", createdBefore=" + createdBefore +
                ", updatedAfter=" + updatedAfter +
                ", updatedBefore=" + updatedBefore +
                '}';
    }
}
