package com.marian_bt.contacts_app.controller;


import com.marian_bt.contacts_app.domain.Contact;
import com.marian_bt.contacts_app.service.ContactSearchCriteria;
import com.marian_bt.contacts_app.service.ContactService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


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
    public String saveContact(@Valid @ModelAttribute("contact") Contact contact,
                              BindingResult bindingResult,
                              Model model) {


        if (bindingResult.hasErrors()) {
            model.addAttribute("hasErrors", true);
            return "contacts/form";
        }
        if (contact.getId() == null) {

            contactService.createContact(contact);
        } else {

            contactService.updateContact(contact.getId(), contact);
        }

        return "redirect:/contacts";
    }

    @PostMapping("/{id}/delete")
    public String deleteContact (@PathVariable("id") Long id){

        contactService.deleteContact(id);
        return "redirect:/contacts";
    }

    @GetMapping("/{id}")
    public String viewContact(@PathVariable("id") Long id, Model model){
        Contact contact = contactService.getContactById(id);
        model.addAttribute("contact", contact);
        return "contacts/detail";
    }

    @GetMapping("/export")
    public void exportContacts(@ModelAttribute ContactSearchCriteria criteria, HttpServletResponse response) throws IOException {

        if (criteria == null) {
            criteria = new ContactSearchCriteria();
        }

        List<Contact> contacts = contactService.searchContacts(criteria);

        response.setContentType("text/csv;charset=UTF-8");
        String fileName = "contacts-" + LocalDateTime.now() + ".csv";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try(PrintWriter writer = response.getWriter()) {
            writer.println("id,title,firstName,lastName,gender,email,phone1,phone2,institution,faculty,studyDomain,persGroup,function,country,coilExp,mobilityFin,createdAt,updatedAt");
            for (Contact c : contacts) {
                writer.print(safe(c.getId()));
                writer.print(',');
                writer.print(csv(c.getTitle()));           writer.print(',');
                writer.print(csv(c.getFirstName()));       writer.print(',');
                writer.print(csv(c.getLastName()));        writer.print(',');
                writer.print(csv(c.getGender()));          writer.print(',');
                writer.print(csv(c.getEmail()));           writer.print(',');
                writer.print(csv(c.getPhone1()));          writer.print(',');
                writer.print(csv(c.getPhone2()));          writer.print(',');
                writer.print(csv(c.getInstitution()));     writer.print(',');
                writer.print(csv(c.getFaculty()));         writer.print(',');
                writer.print(csv(c.getStudyDomain()));     writer.print(',');
                writer.print(csv(c.getPersGroup()));       writer.print(',');
                writer.print(csv(c.getFunction()));        writer.print(',');
                writer.print(csv(c.getCountry()));         writer.print(',');
                writer.print(csv(Boolean.toString(c.isCoilExp())));    writer.print(',');
                writer.print(csv(Boolean.toString(c.isMobilityFin()))); writer.print(',');

                LocalDateTime createdAt = c.getCreatedAt();
                LocalDateTime updatedAt = c.getUpdatedAt();

                writer.print(csv(createdAt != null ? dtf.format(createdAt) : ""));
                writer.print(',');
                writer.print(csv(updatedAt != null ? dtf.format(updatedAt) : ""));

                writer.println();
            }
        }
    }

    private String safe(Object value){
        return value == null ? "" : value.toString();
    }

    private String csv(String value){
        if(value == null) return "\"\"";
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}
