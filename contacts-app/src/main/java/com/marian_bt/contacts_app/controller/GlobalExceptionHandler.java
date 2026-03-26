package com.marian_bt.contacts_app.controller;

import com.marian_bt.contacts_app.service.ContactNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ContactNotFoundException.class)
    public String handleContactNotFoundException(ContactNotFoundException ex, Model model){
        log.warn("Contact not found: {}", ex.getMessage());
        model.addAttribute("message", ex.getMessage());
        return "error/contact-not-found";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model){
        log.error("Unexpected error occurred", ex);
        model.addAttribute("message", "An unexpected error occurred");
        return "error/generic-error";
    }
}
