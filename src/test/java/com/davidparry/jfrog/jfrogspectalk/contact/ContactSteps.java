package com.davidparry.jfrog.jfrogspectalk.contact;

import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Step definitions binding the Gherkin scenarios in
 * {@code src/test/resources/features} to the Contacts HTTP API.
 *
 * <p>REQ-002 CSV-export steps are added during Exercise 2 once JFrog MCP
 * has approved a library — keep this vocabulary small and reuse it.
 */
public class ContactSteps {

    @Autowired
    private MockMvc mockMvc;

    private MvcResult lastResult;
    private Long lastId;

    @Given("the contacts API is running")
    public void theContactsApiIsRunning() {
        // Spring Boot test context + MockMvc are already up.
    }

    @Given("a stored contact named {string} {string} with email {string}")
    public void aStoredContact(String firstName, String lastName, String email) throws Exception {
        createContact(firstName, lastName, email);
        assertThat(lastResult.getResponse().getStatus()).isEqualTo(201);
        lastId = readId();
    }

    @When("I create a contact named {string} {string} with email {string}")
    public void iCreateAContact(String firstName, String lastName, String email) throws Exception {
        createContact(firstName, lastName, email);
        if (lastResult.getResponse().getStatus() == 201) {
            lastId = readId();
        }
    }

    @When("I fetch that contact by id")
    public void iFetchThatContactById() throws Exception {
        lastResult = mockMvc.perform(get("/api/contacts/{id}", lastId)).andReturn();
    }

    @When("I list contacts")
    public void iListContacts() throws Exception {
        lastResult = mockMvc.perform(get("/api/contacts")).andReturn();
    }

    @Then("the response status is {int}")
    public void theResponseStatusIs(int status) {
        assertThat(lastResult.getResponse().getStatus()).isEqualTo(status);
    }

    @Then("the contact email is {string}")
    public void theContactEmailIs(String email) throws Exception {
        String body = lastResult.getResponse().getContentAsString();
        assertThat(JsonPath.<String>read(body, "$.email")).isEqualTo(email);
    }

    @Then("the contact list is not empty")
    public void theContactListIsNotEmpty() throws Exception {
        String body = lastResult.getResponse().getContentAsString();
        int size = JsonPath.read(body, "$.content.length()");
        assertThat(size).isPositive();
    }

    private void createContact(String firstName, String lastName, String email) throws Exception {
        String json = """
                {"firstName":"%s","lastName":"%s","email":"%s"}
                """.formatted(firstName, lastName, email);
        lastResult = mockMvc.perform(post("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();
    }

    private Long readId() throws Exception {
        Number id = JsonPath.read(lastResult.getResponse().getContentAsString(), "$.id");
        return id.longValue();
    }
}
