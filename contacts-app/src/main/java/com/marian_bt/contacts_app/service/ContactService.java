package com.marian_bt.contacts_app.service;

import com.marian_bt.contacts_app.domain.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.InputStream;
import java.util.List;

public interface ContactService {

    List<Contact> getAllContacts();

    Page<Contact> getAllContacts(Pageable pageable);

    Contact getContactById(Long id);

    Contact createContact(Contact contact, String currentUsername);

    Contact updateContact(Long id, Contact updatedContact, String currentUsername);

    void deleteContact(Long id, String currentUsername);

    List<Contact> searchContacts(ContactSearchCriteria criteria);

    Page<Contact> searchContacts(ContactSearchCriteria criteria, Pageable pageable);

    int importContacts (InputStream inputStream);
}
