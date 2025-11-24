package com.marian_bt.contacts_app.controller;


import com.marian_bt.contacts_app.domain.Contact;
import com.marian_bt.contacts_app.service.ContactSearchCriteria;
import com.marian_bt.contacts_app.service.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("contacts")

public class ContactController  {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public String listContacts(@PageableDefault(size = 20) Pageable pageable, Model model)
    {
        Page<Contact> page = contactService.getAllContacts(pageable);
        model.addAttribute("contactsPage", page);
        model.addAttribute("contacts", page.getContent());
        model.addAttribute("criteria", new ContactSearchCriteria());
        model.addAttribute("baseUrl", "/contacts");
        return "contacts/list";
    }

    @GetMapping("/search")
    public String SearchContacts(@ModelAttribute("criteria") ContactSearchCriteria criteria,
                                 @PageableDefault(size  = 20) Pageable pageable ,
                                 Model model)
    {
        Page<Contact> page = contactService.searchContacts(criteria, pageable);
        model.addAttribute("contactsPage", page);
        model.addAttribute("contacts", page.getContent());
        model.addAttribute("criteria", criteria);
        model.addAttribute("baseUrl", "/contacts/search");
        return "contacts/list";
    }

    @GetMapping("/new")
    public String ShowCreateContactForm(Model model)
    {
        Contact contact = new Contact();
        model.addAttribute("contact", contact);
        return "contacts/form";
    }


    @GetMapping("/{id}/edit")
    public String ShowEditContactForm(@PathVariable("id") Long id, Model model)
    {
        Contact contact = contactService.getContactById(id);
        model.addAttribute("contact", contact);
        return "contacts/form";
    }

    @PostMapping("/save")
    public String saveContact(@ModelAttribute("contact") Contact contact) {
        String currentUsername = "system"; // later from Spring Security

        if (contact.getId() == null) {
            // no id -> create
            contactService.createContact(contact, currentUsername);
        } else {
            // id present -> update
            contactService.updateContact(contact.getId(), contact, currentUsername);
        }

        return "redirect:/contacts";
    }

    @PostMapping("/{id}/delete")
    public String deleteContact (@PathVariable("id") Long id){
        String currentUsername = "system";
        contactService.deleteContact(id, currentUsername);
        return "redirect:/contacts";
    }

    @GetMapping("/{id}")
    public String viewContact(@PathVariable("id") Long id, Model model){
        Contact contact = contactService.getContactById(id);
        model.addAttribute("contact", contact);
        return "contacts/detail";
    }

}
