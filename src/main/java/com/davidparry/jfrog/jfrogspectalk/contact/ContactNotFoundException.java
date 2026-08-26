package com.davidparry.jfrog.jfrogspectalk.contact;

public class ContactNotFoundException extends RuntimeException {

    private final Long id;

    public ContactNotFoundException(Long id) {
        super("No contact found with id " + id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
