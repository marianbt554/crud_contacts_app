package com.marian_bt.contacts_app.service;

import com.marian_bt.contacts_app.domain.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
public interface ContactService {

    /*Return all contacts method*/
    List <Contact> getAllContacts();

    /*Find a single contact by the ID*/
    Contact getContactById (Long id);

    /*Create a new contact method*/
    Contact createContact(Contact contact);

    /*Update an existing contact method*/
    Contact updateContact(Long id, Contact updatedContact);


    void deleteContact(Long id);


    List<Contact> searchContacts(ContactSearchCriteria criteria);

    Page<Contact> getAllContacts(Pageable pageable);

    Page<Contact> searchContacts(ContactSearchCriteria criteria, Pageable pageable);


}
