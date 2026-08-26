package com.davidparry.jfrog.jfrogspectalk.contact;

import com.davidparry.jfrog.jfrogspectalk.contact.dto.ContactRequest;
import com.davidparry.jfrog.jfrogspectalk.contact.dto.ContactResponse;
import com.davidparry.jfrog.jfrogspectalk.contact.dto.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@Transactional
public class ContactService {

    private final ContactRepository repository;

    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    public ContactResponse create(ContactRequest request) {
        String email = request.email().trim();
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateEmailException(email);
        }
        Contact contact = new Contact();
        ContactMapper.apply(request, contact);
        return ContactMapper.toResponse(repository.save(contact));
    }

    @Transactional(readOnly = true)
    public ContactResponse findById(Long id) {
        return ContactMapper.toResponse(require(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<ContactResponse> search(String query, Pageable pageable) {
        Page<Contact> page = (query == null || query.isBlank())
                ? repository.findAll(pageable)
                : repository.search(likePattern(query), pageable);
        return PageResponse.from(page, ContactMapper::toResponse);
    }

    public ContactResponse replace(Long id, ContactRequest request) {
        Contact contact = require(id);
        String email = request.email().trim();
        if (repository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new DuplicateEmailException(email);
        }
        ContactMapper.apply(request, contact);
        return ContactMapper.toResponse(repository.save(contact));
    }

    public void delete(Long id) {
        repository.delete(require(id));
    }

    private Contact require(Long id) {
        return repository.findById(id).orElseThrow(() -> new ContactNotFoundException(id));
    }

    private static String likePattern(String query) {
        String escaped = query.trim()
                .toLowerCase(Locale.ROOT)
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
        return "%" + escaped + "%";
    }
}
