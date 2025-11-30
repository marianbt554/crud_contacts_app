package com.marian_bt.contacts_app.service;

import com.marian_bt.contacts_app.domain.Contact;
import com.marian_bt.contacts_app.repository.ContactRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public List<Contact> getAllContacts() {
        List<Contact> all = contactRepository.findAll();
        all.sort(Comparator
                .comparing(Contact::getLastName, Comparator.nullsLast(String::compareToIgnoreCase))
                .thenComparing(Contact::getFirstName, Comparator.nullsLast(String::compareToIgnoreCase)));
        return all;
    }

    @Override
    public Contact getContactById(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with Id " + id));
    }

    @Override
    public Contact createContact(Contact contact, String currentUsername) {

        return contactRepository.save(contact);
    }

    @Override
    public Contact updateContact(Long id, Contact updatedContact, String currentUsername) {
        Contact existing = contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));

        existing.setTitle(updatedContact.getTitle());
        existing.setFirstName(updatedContact.getFirstName());
        existing.setLastName(updatedContact.getLastName());
        existing.setPersGroup(updatedContact.getPersGroup());
        existing.setFunction(updatedContact.getFunction());
        existing.setInstitution(updatedContact.getInstitution());
        existing.setFaculty(updatedContact.getFaculty());
        existing.setStudyDomain(updatedContact.getStudyDomain());
        existing.setEmail(updatedContact.getEmail());
        existing.setPhone1(updatedContact.getPhone1());
        existing.setPhone2(updatedContact.getPhone2());
        existing.setPostAddress(updatedContact.getPostAddress());
        existing.setCountry(updatedContact.getCountry());
        existing.setInterest(updatedContact.getInterest());
        existing.setCoilExp(updatedContact.isCoilExp());
        existing.setMobilityFin(updatedContact.isMobilityFin());
        existing.setFundUse(updatedContact.getFundUse());
        existing.setPastEvent(updatedContact.getPastEvent());
        existing.setContactPerson(updatedContact.getContactPerson());
        existing.setGender(updatedContact.getGender());
        existing.setComments(updatedContact.getComments());

        // again: DO NOT set updatedAt/updatedBy, auditing will
        return contactRepository.save(existing);
    }

    @Override
    public void deleteContact(Long id, String currentUsername) {
        if (!contactRepository.existsById(id)) {
            throw new ContactNotFoundException(id);
        }
        contactRepository.deleteById(id);
    }

    @Override
    public List<Contact> searchContacts(ContactSearchCriteria criteria) {
        if (criteria == null || criteria.isEmpty()) {
            return getAllContacts();
        }

        String firstName   = normalize(criteria.getFirstName());
        String lastName    = normalize(criteria.getLastName());
        String institution = normalize(criteria.getInstitution());
        String email       = normalize(criteria.getEmail());
        String persGroup   = normalize(criteria.getPersGroup());
        String country     = normalize(criteria.getCountry());
        String fundUse     = normalize(criteria.getFundUse());
        String postAddress = normalize(criteria.getPostAddress());
        String phone1      = normalize(criteria.getPhone1());
        String phone2      = normalize(criteria.getPhone2());
        String faculty     = normalize(criteria.getFaculty());
        String studyDomain = normalize(criteria.getStudyDomain());
        String gender      = normalize(criteria.getGender());

        LocalDateTime createdAfter  = criteria.getCreatedAfter();
        LocalDateTime createdBefore = criteria.getCreatedBefore();
        LocalDateTime updatedAfter  = criteria.getUpdatedAfter();
        LocalDateTime updatedBefore = criteria.getUpdatedBefore();

        Boolean coilExp     = criteria.getCoilExp();
        Boolean mobilityFin = criteria.getMobilityFin();

        return contactRepository.searchContacts(
                firstName,
                lastName,
                institution,
                email,
                persGroup,
                country,
                fundUse,
                postAddress,
                phone1,
                phone2,
                faculty,
                studyDomain,
                gender,
                coilExp,
                mobilityFin,
                createdAfter,
                createdBefore,
                updatedAfter,
                updatedBefore
        );
    }

    private Pageable withDefaultSort(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            Sort defaultSort = Sort.by(
                    Sort.Order.asc("lastName").ignoreCase(),
                    Sort.Order.asc("firstName").ignoreCase()
            );
            return PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    defaultSort
            );
        }
        return pageable;
    }

    @Override
    public Page<Contact> getAllContacts(Pageable pageable) {
        Pageable sortedPageable = withDefaultSort(pageable);
        return contactRepository.findAll(sortedPageable);
    }

    @Override
    public Page<Contact> searchContacts(ContactSearchCriteria criteria, Pageable pageable) {
        if (criteria == null || criteria.isEmpty()) {
            return getAllContacts(pageable);
        }

        String firstName   = normalize(criteria.getFirstName());
        String lastName    = normalize(criteria.getLastName());
        String institution = normalize(criteria.getInstitution());
        String email       = normalize(criteria.getEmail());
        String persGroup   = normalize(criteria.getPersGroup());
        String country     = normalize(criteria.getCountry());
        String fundUse     = normalize(criteria.getFundUse());
        String postAddress = normalize(criteria.getPostAddress());
        String phone1      = normalize(criteria.getPhone1());
        String phone2      = normalize(criteria.getPhone2());
        String faculty     = normalize(criteria.getFaculty());
        String studyDomain = normalize(criteria.getStudyDomain());
        String gender      = normalize(criteria.getGender());

        LocalDateTime createdAfter  = criteria.getCreatedAfter();
        LocalDateTime createdBefore = criteria.getCreatedBefore();
        LocalDateTime updatedAfter  = criteria.getUpdatedAfter();
        LocalDateTime updatedBefore = criteria.getUpdatedBefore();

        Boolean coilExp     = criteria.getCoilExp();
        Boolean mobilityFin = criteria.getMobilityFin();

        Pageable sortedPageable = withDefaultSort(pageable);

        return contactRepository.searchContactsPaged(
                firstName,
                lastName,
                institution,
                email,
                persGroup,
                country,
                fundUse,
                postAddress,
                phone1,
                phone2,
                faculty,
                studyDomain,
                gender,
                coilExp,
                mobilityFin,
                createdAfter,
                createdBefore,
                updatedAfter,
                updatedBefore,
                sortedPageable
        );
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
