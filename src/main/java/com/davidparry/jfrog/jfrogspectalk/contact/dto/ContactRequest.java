package com.davidparry.jfrog.jfrogspectalk.contact.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload used to create or fully replace a contact.")
public record ContactRequest(

        @NotBlank
        @Size(max = 100)
        @Schema(example = "Ada")
        String firstName,

        @NotBlank
        @Size(max = 100)
        @Schema(example = "Lovelace")
        String lastName,

        @NotBlank
        @Email
        @Size(max = 254)
        @Schema(example = "ada@example.com")
        String email,

        @Pattern(regexp = "^[+()\\-.\\s0-9]{7,30}$", message = "must be a valid phone number")
        @Schema(example = "+1-555-0100")
        String phone,

        @Size(max = 150)
        @Schema(example = "Analytical Engines Ltd")
        String company,

        @Size(max = 2000)
        String notes,

        @Valid
        AddressDto address) {
}
