package com.davidparry.jfrog.jfrogspectalk.contact.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Postal address of a contact. All parts are optional.")
public record AddressDto(

        @Size(max = 200)
        @Schema(example = "1 Infinite Loop")
        String street,

        @Size(max = 100)
        @Schema(example = "Cupertino")
        String city,

        @Size(max = 100)
        @Schema(example = "CA")
        String state,

        @Size(max = 20)
        @Schema(example = "95014")
        String postalCode,

        @Size(max = 100)
        @Schema(example = "USA")
        String country) {
}
