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
import java.util.*;

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
    public int importContacts(InputStream inputStream) {
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            // 1) Read header row
            String headerLine = reader.readLine();
            if (headerLine == null || headerLine.isBlank()) {
                throw new ContactImportException("CSV is empty or missing the header row.");
            }

            // Strip BOM if present and parse as CSV
            headerLine = headerLine.replace("\uFEFF", "");
            List<String> headerCols = parseCsvLine(headerLine);

            // Build header name -> index map (lowercased + trimmed)
            Map<String, Integer> headerIndex = new HashMap<>();
            for (int i = 0; i < headerCols.size(); i++) {
                String raw = headerCols.get(i);
                if (raw == null) continue;
                String normalized = raw.trim().toLowerCase(Locale.ROOT);
                if (!normalized.isEmpty()) {
                    headerIndex.put(normalized, i);
                }
            }

            // We require at least an "email" column so we can identify contacts
            if (!hasAnyHeader(headerIndex, "email", "e-mail")) {
                throw new ContactImportException(
                        "CSV header must contain an 'email' column (exact name: email)."
                );
            }

            int count = 0;
            String line;

            // 2) Process data rows
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                List<String> cols = parseCsvLine(line);

                // --- REQUIRED: email (identifies contact) ---
                String email = getColumn(headerIndex, cols, "email", "e-mail");
                if (email == null || email.isBlank()) {
                    // no email → skip this row (or you could throw if you want it stricter)
                    continue;
                }
                email = email.trim();

                Contact contact = contactRepository
                        .findByEmailIgnoreCase(email)
                        .orElseGet(Contact::new);

                contact.setEmail(email);

                // --- OPTIONAL FIELDS (set only if present) ---
                contact.setTitle(getColumn(headerIndex, cols, "title"));

                contact.setFirstName(getColumn(headerIndex, cols,
                        "firstname", "first_name", "first name"));

                contact.setLastName(getColumn(headerIndex, cols,
                        "lastname", "last_name", "last name"));

                contact.setGender(getColumn(headerIndex, cols, "gender"));

                contact.setPhone1(getColumn(headerIndex, cols,
                        "phone1", "phone_1", "phone"));

                contact.setPhone2(getColumn(headerIndex, cols,
                        "phone2", "phone_2", "mobile", "mobile_phone"));

                contact.setInstitution(getColumn(headerIndex, cols, "institution"));
                contact.setFaculty(getColumn(headerIndex, cols, "faculty"));

                contact.setStudyDomain(getColumn(headerIndex, cols,
                        "studydomain", "study_domain", "study domain"));

                contact.setPersGroup(getColumn(headerIndex, cols,
                        "persgroup", "pers_group", "personal_group", "personal group"));

                contact.setFunction(getColumn(headerIndex, cols,
                        "function", "jobfunction", "job_function", "job function"));

                contact.setCountry(getColumn(headerIndex, cols, "country"));

                String coilExpStr = getColumn(headerIndex, cols,
                        "coilexp", "coil_exp", "coil experience");
                contact.setCoilExp(parseBoolean(coilExpStr));

                String mobilityFinStr = getColumn(headerIndex, cols,
                        "mobilityfin", "mobility_fin", "mobility financing");
                contact.setMobilityFin(parseBoolean(mobilityFinStr));

                // We still ignore createdAt / updatedAt in the CSV – auditing handles them.

                contactRepository.save(contact);
                count++;
            }

            return count;

        } catch (IOException e) {
            throw new ContactImportException("Failed to read CSV file", e);
        }
    }

    private boolean hasAnyHeader(Map<String, Integer> headerIndex, String... names) {
        for (String name : names) {
            if (headerIndex.containsKey(name.toLowerCase(Locale.ROOT))) {
                return true;
            }
        }
        return false;
    }


    private String getColumn(Map<String, Integer> headerIndex,
                             List<String> cols,
                             String... possibleHeaders) {

        for (String candidate : possibleHeaders) {
            Integer idx = headerIndex.get(candidate.toLowerCase(Locale.ROOT));
            if (idx != null && idx < cols.size()) {
                return unquote(cols.get(idx));
            }
        }
        return null;
    }


    private boolean parseBoolean(String value) {
        if (value == null) return false;
        String v = value.trim().toLowerCase(Locale.ROOT);
        return v.equals("true") || v.equals("yes") || v.equals("1") || v.equals("y");
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

