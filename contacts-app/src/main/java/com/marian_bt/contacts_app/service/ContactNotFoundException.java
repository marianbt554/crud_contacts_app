package com.marian_bt.contacts_app.service;

public class ContactNotFoundException extends RuntimeException{
    public ContactNotFoundException(Long id){
        super("Contact with id "+id+" not found");
    }

    public ContactNotFoundException(String message){
        super(message);
    }
}
