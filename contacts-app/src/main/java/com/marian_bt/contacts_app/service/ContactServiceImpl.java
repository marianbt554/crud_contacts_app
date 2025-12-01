package com.marian_bt.contacts_app.service;

import com.marian_bt.contacts_app.domain.Contact;
import com.marian_bt.contacts_app.repository.ContactRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Override
    public int importContacts (InputStream inputStream) {
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String header = reader.readLine(); // first line with column names
            if (header == null) {
                return 0;
            }

            int count = 0;
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                var cols = parseCsvLine(line);

                // Expect at least the columns you export:
                // id, title, firstName, lastName, gender, email, phone1, phone2,
                // institution, faculty, studyDomain, persGroup, function,
                // country, coilExp, mobilityFin, createdAt, updatedAt
                if (cols.size() < 17) {
                    throw new ContactImportException("Invalid column count in line: " + line);
                }

                String email = unquote(cols.get(5));
                if (email == null || email.isBlank()) {
                    // no email → skip (or you could throw)
                    continue;
                }

                Contact contact = contactRepository
                        .findByEmailIgnoreCase(email)
                        .orElseGet(Contact::new);

                // Fill / update fields
                contact.setTitle(unquote(cols.get(1)));
                contact.setFirstName(unquote(cols.get(2)));
                contact.setLastName(unquote(cols.get(3)));
                contact.setGender(unquote(cols.get(4)));
                contact.setEmail(email);
                contact.setPhone1(unquote(cols.get(6)));
                contact.setPhone2(unquote(cols.get(7)));
                contact.setInstitution(unquote(cols.get(8)));
                contact.setFaculty(unquote(cols.get(9)));
                contact.setStudyDomain(unquote(cols.get(10)));
                contact.setPersGroup(unquote(cols.get(11)));
                contact.setFunction(unquote(cols.get(12)));
                contact.setCountry(unquote(cols.get(13)));

                String coilExpStr = unquote(cols.get(14));
                String mobilityFinStr = unquote(cols.get(15));
                contact.setCoilExp("true".equalsIgnoreCase(coilExpStr));
                contact.setMobilityFin("true".equalsIgnoreCase(mobilityFinStr));

                // We ignore createdAt / updatedAt from CSV.
                // Auditing will set them automatically on save.

                contactRepository.save(contact);
                count++;
            }

            return count;

        } catch (IOException e) {
            throw new ContactImportException("Failed to read CSV file", e);
        }
    }

    private java.util.List<String> parseCsvLine(String line) {
        java.util.List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // escaped "
                    sb.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString());
        return result;
    }

    private String unquote(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        if (trimmed.length() >= 2 && trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }
        // unescape ""
        return trimmed.replace("\"\"", "\"");
    }
}

