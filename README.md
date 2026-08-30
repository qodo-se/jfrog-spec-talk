# Contacts API

> **Students: start here → [`student-follow-along.md`](student-follow-along.md)**
> Your step-by-step companion for the hour — the exact commands, the exact
> agent prompts, what you should see at every step, and a self-check.

A hands-on talk. The Contacts CRUD API in this repo is **finished
infrastructure — you use it, you don't rebuild it**. What you drive is
**spec-driven development with JFrog MCP in the loop**: you and your
coding agent draft a CSV-export requirement together, use JFrog MCP *before*
any new library hits the POM, then turn the spec into Gherkin and JUnit
and walk Red → Green.

**Attending?** Follow [`student-follow-along.md`](student-follow-along.md).

**Presenting?** The end-to-end platform map (including post-PR Qodo,
Artifactory, Xray, Evidence, and release bundles) is in
[`talk-plan.md`](talk-plan.md).

## Three altitudes, one discipline

| Methodology | Pins | Canonical artifact | In this repo |
| --- | --- | --- | --- |
| **SDD** (spec-driven) | the feature | versioned spec + acceptance criteria | `requirements/requirements.json` |
| **BDD** (behavior-driven) | one behavior | Gherkin scenario (Given/When/Then) | `src/test/resources/features/contacts.feature` |
| **TDD** (test-driven) | one unit | failing unit test | `ContactApiTest`, JUnit 5 |

JFrog MCP sits **inside spec design**, not after the code exists: no new
dependency until your coding agent has called JFrog MCP.

## What's in the box

| Path | What it is |
| --- | --- |
| `src/main/java/.../contact/` | Finished Contacts CRUD API |
| `requirements/requirements.json` | SDD spec — REQ-001 implemented, REQ-002 pending |
| `.cursor/mcp.json` | Registers `https://trialiqsxt4.jfrog.io/mcp` with Cursor (OAuth) |
| `.mcp.json` | Same URL for Claude-style clients |
| `.cursor/rules/jfrog-spec-driven.mdc` | Always-on rule: JFrog MCP before any new Maven dependency |
| `student-follow-along.md` | Attendee companion |
| `scripts/verify-talk-run.sh` | Grades Exercise 1 + 2 |

## Prerequisites

- Java 21+
- A coding agent that can host MCP (Cursor, Claude Desktop, or similar)
- Optional: JFrog CLI, if you want `jf mcp install --agent cursor` instead of the committed JSON

## Setup

```bash
git clone <this repo> && cd jfrog-spec-talk
git checkout -b talk
./mvnw -q package
```

Approve `jfrog` in your coding agent's MCP settings (Cursor: **Settings →
MCP**) and complete OAuth. Details in
[`student-follow-along.md`](student-follow-along.md).
