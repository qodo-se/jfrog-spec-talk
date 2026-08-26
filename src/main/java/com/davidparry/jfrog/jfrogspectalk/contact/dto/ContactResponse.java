package com.davidparry.jfrog.jfrogspectalk.contact.dto;

import java.time.Instant;

public record ContactResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String company,
        String notes,
        AddressDto address,
        Instant createdAt,
        Instant updatedAt) {
}
