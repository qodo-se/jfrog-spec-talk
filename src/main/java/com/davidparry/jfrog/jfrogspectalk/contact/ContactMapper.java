package com.davidparry.jfrog.jfrogspectalk.contact;

import com.davidparry.jfrog.jfrogspectalk.contact.dto.AddressDto;
import com.davidparry.jfrog.jfrogspectalk.contact.dto.ContactRequest;
import com.davidparry.jfrog.jfrogspectalk.contact.dto.ContactResponse;

/**
 * Translates between the {@link Contact} entity and its transport representations.
 */
public final class ContactMapper {

    private ContactMapper() {
    }

    public static ContactResponse toResponse(Contact contact) {
        return new ContactResponse(
                contact.getId(),
                contact.getFirstName(),
                contact.getLastName(),
                contact.getEmail(),
                contact.getPhone(),
                contact.getCompany(),
                contact.getNotes(),
                toAddressDto(contact.getAddress()),
                contact.getCreatedAt(),
                contact.getUpdatedAt());
    }

    /**
     * Copies every writable field from the request onto the entity. Used for both create and full
     * replace, so absent optional fields are cleared rather than left behind.
     */
    public static void apply(ContactRequest request, Contact contact) {
        contact.setFirstName(request.firstName().trim());
        contact.setLastName(request.lastName().trim());
        contact.setEmail(request.email().trim());
        contact.setPhone(request.phone());
        contact.setCompany(request.company());
        contact.setNotes(request.notes());
        contact.setAddress(toAddress(request.address()));
    }

    private static AddressDto toAddressDto(Address address) {
        if (address == null || address.isEmpty()) {
            return null;
        }
        return new AddressDto(
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.getCountry());
    }

    private static Address toAddress(AddressDto dto) {
        if (dto == null) {
            return null;
        }
        return new Address(dto.street(), dto.city(), dto.state(), dto.postalCode(), dto.country());
    }
}
