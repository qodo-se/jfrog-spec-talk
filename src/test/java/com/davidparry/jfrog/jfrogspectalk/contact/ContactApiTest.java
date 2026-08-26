package com.davidparry.jfrog.jfrogspectalk.contact;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit-level (TDD) tests for the Contacts API. The behavior-level (BDD) spec
 * lives in {@code src/test/resources/features/contacts.feature} and runs
 * through Cucumber via {@code RunCucumberTest}.
 *
 * <p>REQ-002 tests are generated live from acceptance criteria during
 * Exercise 2, after JFrog MCP has approved a CSV library.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ContactApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("REQ-001: a new contact can be created")
    void createContact() throws Exception {
        mockMvc.perform(post("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName":"Talk","lastName":"Create","email":"talk.create@example.com"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("talk.create@example.com"));
    }

    @Test
    @DisplayName("REQ-001: a contact can be fetched by id")
    void fetchContactById() throws Exception {
        MvcResult created = mockMvc.perform(post("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName":"Talk","lastName":"Get","email":"talk.get@example.com"}
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        String id = JsonPath.read(
                created.getResponse().getContentAsString(), "$.id").toString();

        mockMvc.perform(get("/api/contacts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("talk.get@example.com"));
    }

    // REQ-002: tests are generated live from acceptance criteria by the
    // agent during Exercise 2. Draft the spec (and chosenPackage) first.
}
