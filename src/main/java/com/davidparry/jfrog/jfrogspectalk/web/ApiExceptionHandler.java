package com.davidparry.jfrog.jfrogspectalk.web;

import com.davidparry.jfrog.jfrogspectalk.contact.ContactNotFoundException;
import com.davidparry.jfrog.jfrogspectalk.contact.DuplicateEmailException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Renders every error as an RFC 9457 problem detail.
 *
 * <p>Extending {@link ResponseEntityExceptionHandler} makes Spring Boot's auto-configured
 * {@code ProblemDetailsExceptionHandler} back off, so this is the single handler in play. The base
 * class already covers framework exceptions (validation, unreadable body, unsupported media type,
 * missing parameters); the methods below add the application's own failure modes.
 */
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String PROBLEM_BASE = "https://jfrog-spec-talk.example/problems/";

    @ExceptionHandler(ContactNotFoundException.class)
    public ProblemDetail handleContactNotFound(ContactNotFoundException ex, HttpServletRequest request) {
        return problem(HttpStatus.NOT_FOUND, "Contact not found", ex.getMessage(), "contact-not-found", request);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ProblemDetail handleDuplicateEmail(DuplicateEmailException ex, HttpServletRequest request) {
        ProblemDetail problem =
                problem(HttpStatus.CONFLICT, "Duplicate email", ex.getMessage(), "duplicate-email", request);
        problem.setProperty("email", ex.getEmail());
        return problem;
    }

    /**
     * Safety net for the race where two requests pass the pre-check and the unique constraint fires.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex,
            HttpServletRequest request) {
        return problem(HttpStatus.CONFLICT, "Conflicting data",
                "The request conflicts with an existing record.", "data-conflict", request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problem = ex.getBody();
        problem.setType(URI.create(PROBLEM_BASE + "validation-failed"));
        problem.setTitle("Validation failed");
        problem.setDetail("The request body has %d invalid field(s)."
                .formatted(ex.getBindingResult().getErrorCount()));

        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> merge(errors, error.getField(), error.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors()
                .forEach(error -> merge(errors, error.getObjectName(), error.getDefaultMessage()));
        problem.setProperty("errors", errors);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private static void merge(Map<String, String> errors, String key, String message) {
        errors.merge(key, message == null ? "is invalid" : message, (a, b) -> a + "; " + b);
    }

    private static ProblemDetail problem(HttpStatus status, String title, String detail, String type,
            HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setType(URI.create(PROBLEM_BASE + type));
        problem.setInstance(URI.create(request.getRequestURI()));
        return problem;
    }
}
