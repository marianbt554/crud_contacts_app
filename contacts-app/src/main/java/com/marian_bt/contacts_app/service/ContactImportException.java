package com.marian_bt.contacts_app.service;

public class ContactImportException extends RuntimeException{
    public ContactImportException(String message) {
        super(message);
    }

    public ContactImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
