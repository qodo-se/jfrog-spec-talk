# The executable behavior spec (BDD level) for the Contacts API.
#
# Each scenario is tagged with the requirement it verifies. Scenarios for
# REQ-001 ship as the worked example (the CRUD API is finished infrastructure).
# During the talk the agent appends REQ-002 scenarios from the acceptance
# criteria in requirements/requirements.json — after JFrog MCP has approved
# a CSV library. The human reviews the spec, then the loop runs RED -> GREEN.
Feature: Contacts API
  As an operator of the contacts service
  I want to manage contacts over HTTP
  So that the address book stays accurate

  @REQ-001
  Scenario: A new contact can be created
    Given the contacts API is running
    When I create a contact named "Talk" "User" with email "talk.user@example.com"
    Then the response status is 201
    And the contact email is "talk.user@example.com"

  @REQ-001
  Scenario: A contact can be fetched by id
    Given a stored contact named "Talk" "Fetch" with email "talk.fetch@example.com"
    When I fetch that contact by id
    Then the response status is 200
    And the contact email is "talk.fetch@example.com"

  @REQ-001
  Scenario: Contacts can be listed
    Given the contacts API is running
    When I list contacts
    Then the response status is 200
    And the contact list is not empty

  # REQ-002: CSV export — scenarios are written live during Exercise 2
  # after JFrog MCP approves a library. Draft REQ-002 in the spec first.
