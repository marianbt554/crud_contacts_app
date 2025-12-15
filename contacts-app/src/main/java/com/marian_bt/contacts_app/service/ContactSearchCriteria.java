package com.marian_bt.contacts_app.service;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class ContactSearchCriteria {

    private String title;
    private String firstName;
    private String lastName;
    private String gender;

    private String email;
    private String phone1;
    private String phone2;
    private String postAddress;

    private String institution;
    private String faculty;
    private String studyDomain;
    private String persGroup;
    private String function;

    private String country;
    private String interest;
    private String fundUse;
    private String pastEvent;
    private String comments;

    private Boolean contactedByIngenium;
    private Boolean coilExp;
    private Boolean mobilityFin;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdAfter;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdBefore;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime updatedAfter;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime updatedBefore;

    private static String trimToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    // --- Normalizing setters (important!) ---
    public void setTitle(String title) { this.title = trimToNull(title); }
    public void setFirstName(String firstName) { this.firstName = trimToNull(firstName); }
    public void setLastName(String lastName) { this.lastName = trimToNull(lastName); }
    public void setGender(String gender) { this.gender = trimToNull(gender); }

    public void setEmail(String email) { this.email = trimToNull(email); }
    public void setPhone1(String phone1) { this.phone1 = trimToNull(phone1); }
    public void setPhone2(String phone2) { this.phone2 = trimToNull(phone2); }
    public void setPostAddress(String postAddress) { this.postAddress = trimToNull(postAddress); }

    public void setInstitution(String institution) { this.institution = trimToNull(institution); }
    public void setFaculty(String faculty) { this.faculty = trimToNull(faculty); }
    public void setStudyDomain(String studyDomain) { this.studyDomain = trimToNull(studyDomain); }
    public void setPersGroup(String persGroup) { this.persGroup = trimToNull(persGroup); }
    public void setFunction(String function) { this.function = trimToNull(function); }

    public void setCountry(String country) { this.country = trimToNull(country); }
    public void setInterest(String interest) { this.interest = trimToNull(interest); }
    public void setFundUse(String fundUse) { this.fundUse = trimToNull(fundUse); }
    public void setPastEvent(String pastEvent) { this.pastEvent = trimToNull(pastEvent); }
    public void setComments(String comments) { this.comments = trimToNull(comments); }

    // --- Getters ---
    public String getTitle() { return title; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getGender() { return gender; }

    public String getEmail() { return email; }
    public String getPhone1() { return phone1; }
    public String getPhone2() { return phone2; }
    public String getPostAddress() { return postAddress; }

    public String getInstitution() { return institution; }
    public String getFaculty() { return faculty; }
    public String getStudyDomain() { return studyDomain; }
    public String getPersGroup() { return persGroup; }
    public String getFunction() { return function; }

    public String getCountry() { return country; }
    public String getInterest() { return interest; }
    public String getFundUse() { return fundUse; }
    public String getPastEvent() { return pastEvent; }
    public String getComments() { return comments; }

    public Boolean getContactedByIngenium() { return contactedByIngenium; }
    public void setContactedByIngenium(Boolean contactedByIngenium) { this.contactedByIngenium = contactedByIngenium; }

    public Boolean getCoilExp() { return coilExp; }
    public void setCoilExp(Boolean coilExp) { this.coilExp = coilExp; }

    public Boolean getMobilityFin() { return mobilityFin; }
    public void setMobilityFin(Boolean mobilityFin) { this.mobilityFin = mobilityFin; }

    public LocalDateTime getCreatedAfter() { return createdAfter; }
    public void setCreatedAfter(LocalDateTime createdAfter) { this.createdAfter = createdAfter; }

    public LocalDateTime getCreatedBefore() { return createdBefore; }
    public void setCreatedBefore(LocalDateTime createdBefore) { this.createdBefore = createdBefore; }

    public LocalDateTime getUpdatedAfter() { return updatedAfter; }
    public void setUpdatedAfter(LocalDateTime updatedAfter) { this.updatedAfter = updatedAfter; }

    public LocalDateTime getUpdatedBefore() { return updatedBefore; }
    public void setUpdatedBefore(LocalDateTime updatedBefore) { this.updatedBefore = updatedBefore; }

    // --- Helpers ---
    public boolean isEmpty() {
        return isBlank(title)
                && isBlank(firstName)
                && isBlank(lastName)
                && isBlank(gender)
                && isBlank(email)
                && isBlank(phone1)
                && isBlank(phone2)
                && isBlank(postAddress)
                && isBlank(institution)
                && isBlank(faculty)
                && isBlank(studyDomain)
                && isBlank(persGroup)
                && isBlank(function)
                && isBlank(country)
                && isBlank(interest)
                && isBlank(fundUse)
                && isBlank(pastEvent)
                && isBlank(comments)
                && contactedByIngenium == null
                && coilExp == null
                && mobilityFin == null
                && createdAfter == null
                && createdBefore == null
                && updatedAfter == null
                && updatedBefore == null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "ContactSearchCriteria{" +
                "title='" + title + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", phone1='" + phone1 + '\'' +
                ", phone2='" + phone2 + '\'' +
                ", postAddress='" + postAddress + '\'' +
                ", institution='" + institution + '\'' +
                ", faculty='" + faculty + '\'' +
                ", studyDomain='" + studyDomain + '\'' +
                ", persGroup='" + persGroup + '\'' +
                ", function='" + function + '\'' +
                ", country='" + country + '\'' +
                ", interest='" + interest + '\'' +
                ", fundUse='" + fundUse + '\'' +
                ", pastEvent='" + pastEvent + '\'' +
                ", comments='" + comments + '\'' +
                ", contactedByIngenium=" + contactedByIngenium +
                ", coilExp=" + coilExp +
                ", mobilityFin=" + mobilityFin +
                ", createdAfter=" + createdAfter +
                ", createdBefore=" + createdBefore +
                ", updatedAfter=" + updatedAfter +
                ", updatedBefore=" + updatedBefore +
                '}';
    }
}
