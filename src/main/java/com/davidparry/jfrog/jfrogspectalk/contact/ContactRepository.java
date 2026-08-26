package com.davidparry.jfrog.jfrogspectalk.contact;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    /**
     * Case-insensitive contains-match across name, email and company.
     *
     * @param pattern an already lower-cased SQL LIKE pattern, e.g. {@code %ada%}
     */
    @Query("""
            select c from Contact c
            where lower(c.firstName) like :pattern
               or lower(c.lastName) like :pattern
               or lower(c.email) like :pattern
               or lower(coalesce(c.company, '')) like :pattern
            """)
    Page<Contact> search(@Param("pattern") String pattern, Pageable pageable);
}
