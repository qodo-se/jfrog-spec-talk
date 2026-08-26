package com.davidparry.jfrog.jfrogspectalk.contact;

import com.davidparry.jfrog.jfrogspectalk.contact.dto.ContactRequest;
import com.davidparry.jfrog.jfrogspectalk.contact.dto.ContactResponse;
import com.davidparry.jfrog.jfrogspectalk.contact.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/contacts")
@Tag(name = "Contacts", description = "Create, read, update, delete and search contacts")
public class ContactController {

    private final ContactService service;

    public ContactController(ContactService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create a contact")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "409", description = "Email already in use", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    public ResponseEntity<ContactResponse> create(@Valid @RequestBody ContactRequest request) {
        ContactResponse created = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch a single contact by id")
    public ContactResponse get(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    @Operation(summary = "List contacts, optionally filtered by a free-text query")
    public PageResponse<ContactResponse> list(
            @Parameter(description = "Case-insensitive match against first name, last name, email or company")
            @RequestParam(name = "q", required = false) String q,
            @PageableDefault(size = 20, sort = "lastName", direction = Sort.Direction.ASC) Pageable pageable) {
        return service.search(q, pageable);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace an existing contact")
    public ContactResponse replace(@PathVariable Long id, @Valid @RequestBody ContactRequest request) {
        return service.replace(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a contact")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
