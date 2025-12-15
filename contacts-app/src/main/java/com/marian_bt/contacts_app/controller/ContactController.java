package com.marian_bt.contacts_app.controller;

import com.marian_bt.contacts_app.domain.Contact;
import com.marian_bt.contacts_app.service.ContactImportException;
import com.marian_bt.contacts_app.service.ContactSearchCriteria;
import com.marian_bt.contacts_app.service.ContactService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }


    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return "system";
        }
        return auth.getName();
    }

    @GetMapping
    public String listContacts(@PageableDefault(size = 20) Pageable pageable, Model model) {
        Page<Contact> page = contactService.getAllContacts(pageable);
        model.addAttribute("contactsPage", page);
        model.addAttribute("contacts", page.getContent());
        model.addAttribute("criteria", new ContactSearchCriteria());
        model.addAttribute("baseUrl", "/contacts");
        return "contacts/list";
    }

    @GetMapping("/search")
    public String SearchContacts(@ModelAttribute("criteria") ContactSearchCriteria criteria,
                                 @PageableDefault(size = 20) Pageable pageable,
                                 Model model) {
        Page<Contact> page = contactService.searchContacts(criteria, pageable);
        model.addAttribute("contactsPage", page);
        model.addAttribute("contacts", page.getContent());
        model.addAttribute("criteria", criteria);
        model.addAttribute("baseUrl", "/contacts/search");
        return "contacts/list";
    }

    @GetMapping("/new")
    public String ShowCreateContactForm(Model model) {
        Contact contact = new Contact();
        model.addAttribute("contact", contact);
        return "contacts/form";
    }

    @GetMapping("/{id}/edit")
    public String ShowEditContactForm(@PathVariable("id") Long id, Model model) {
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

        String currentUsername = getCurrentUsername();

        if (contact.getId() == null) {
            // create
            contactService.createContact(contact, currentUsername);
        } else {
            // update
            contactService.updateContact(contact.getId(), contact, currentUsername);
        }

        return "redirect:/contacts";
    }

    @PostMapping("/{id}/delete")
    public String deleteContact(@PathVariable("id") Long id) {
        String currentUsername = getCurrentUsername();   // for future logging if needed
        contactService.deleteContact(id, currentUsername);
        return "redirect:/contacts";
    }

    @GetMapping("/{id}")
    public String viewContact(@PathVariable("id") Long id, Model model) {
        Contact contact = contactService.getContactById(id);
        model.addAttribute("contact", contact);
        return "contacts/detail";
    }

    @GetMapping("/export")
    public void exportContacts(@ModelAttribute ContactSearchCriteria criteria,
                               HttpServletResponse response) throws IOException {

        if (criteria == null) {
            criteria = new ContactSearchCriteria();
        }

        List<Contact> contacts = contactService.searchContacts(criteria);

        response.setContentType("text/csv;charset=UTF-8");
        String fileName = "contacts-" + LocalDateTime.now() + ".csv";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("id,title,firstName,lastName,gender,email,phone1,phone2,institution,faculty,studyDomain,persGroup,function,country,coilExp,mobilityFin,createdAt,updatedAt");
            for (Contact c : contacts) {
                writer.print(safe(c.getId()));           writer.print(',');
                writer.print(csv(c.getTitle()));         writer.print(',');
                writer.print(csv(c.getFirstName()));     writer.print(',');
                writer.print(csv(c.getLastName()));      writer.print(',');
                writer.print(csv(c.getGender()));        writer.print(',');
                writer.print(csv(c.getEmail()));         writer.print(',');
                writer.print(csv(c.getPhone1()));        writer.print(',');
                writer.print(csv(c.getPhone2()));        writer.print(',');
                writer.print(csv(c.getInstitution()));   writer.print(',');
                writer.print(csv(c.getFaculty()));       writer.print(',');
                writer.print(csv(c.getStudyDomain()));   writer.print(',');
                writer.print(csv(c.getPersGroup()));     writer.print(',');
                writer.print(csv(c.getFunction()));      writer.print(',');
                writer.print(csv(c.getCountry()));       writer.print(',');
                writer.print(csv(Boolean.toString(c.isCoilExp())));     writer.print(',');
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

    private String safe(Object value) {
        return value == null ? "" : value.toString();
    }

    private String csv(String value) {
        if (value == null) return "\"\"";
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }

    @GetMapping("/import")
    @PreAuthorize("hasRole('ADMIN')")
    public String showImportForm() {
        return "contacts/import";
    }

    @PostMapping("/import")
    @PreAuthorize("hasRole('ADMIN')")
    public String handleImport(@RequestParam("file") MultipartFile file,
                               RedirectAttributes redirectAttributes) {

        if (file == null || file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Please select a CSV file to upload.");
            return "redirect:/contacts/import";
        }

        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase(Locale.ROOT).endsWith(".csv")) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "The uploaded file must be a .csv file.");
            return "redirect:/contacts/import";
        }

        try (InputStream is = file.getInputStream()) {

            int imported = contactService.importContacts(is);

            if (imported > 0) {
                redirectAttributes.addFlashAttribute("message",
                        imported + " contacts imported successfully.");
            } else {
                // File was syntactically OK, but no usable rows (e.g. no email values)
                redirectAttributes.addFlashAttribute("message",
                        "The file was processed, but no contacts were imported. " +
                                "Make sure there is an 'email' column and that rows have email values.");
            }

            return "redirect:/contacts";

        } catch (ContactImportException e) {
            // Domain-level errors (missing headers, invalid values, etc.)
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Import failed: " + e.getMessage());
            return "redirect:/contacts/import";

        } catch (IOException e) {
            // Low-level IO errors
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Could not read the uploaded file: " + e.getMessage());
            return "redirect:/contacts/import";
        }
    }

}
