package com.marian_bt.contacts_app.service;

import com.marian_bt.contacts_app.domain.Contact;
import java.util.List;
public interface ContactService {

    /*Return all contacts method*/
    List <Contact> getAllContacts();

    /*Find a single contact by the ID*/
    List <Contact> getContactById (Long id);

    /*Create a new contact method*/
    Contact createContact(Contact contact, String currentUsername);

    /*Update an existing contact method*/
    Contact updateContact(Long id, Contact updatedContact, String currentUsername);

    /*Delete an existing contact method*/
    void deleteContact(Long id, String currentUsername);

    /*Search contacts using multiple search criteria*/
    List<Contact> searchContacts(ContactSearchCriteria searchCriteria);




}
