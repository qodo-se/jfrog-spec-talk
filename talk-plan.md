# Talk Plan

**Students:** follow [`student-follow-along.md`](student-follow-along.md) —
commands, paste-ready prompts, expected output, and the self-check. This
page is the presenter map, including what happens *after* GREEN.

## End-to-End Flow

```text
          Business Requirement
                  │
                  ▼
        Approved Specification
       Acceptance Criteria / BDD
                  │
                  ▼
        Spec-Driven Development
                  │
                  ▼
            Coding Agent
                  │
                  │ JFrog MCP
                  ├──────────────► Approved packages
                  │               Vulnerabilities
                  │               Curation policy
                  ▼
          Implementation
                  │
                  ▼
                PR
                  │
                  ▼
           Qodo Code Review
     ┌────────────┼────────────┐
     │            │            │
 Specification  Codebase    Standards
  compliance     context      / rules
     │            │            │
     └────────────┼────────────┘
                  │
                  ▼
                MERGE
                  │
                  ▼
                Build
                  │
                  ▼
          JFrog Artifactory
                  │
          ┌───────┼────────┐
          │       │        │
        Xray    SBOM    Evidence
          │                ▲
          │                │
          └────── Qodo review
                 attestation
                  │
                  ▼
             Release Bundle
                  │
                  ▼
              Production
```

The hour on stage stops at GREEN (Exercise 2 in the follow-along). Qodo
review, merge, Artifactory, Xray, SBOM, Evidence, and the release bundle
are the rest of the platform story — narrative after the PR, not a live
lab step.

**Laptop / JPD prep (not a student paste):** Catalog and Curation MCP
tools need Unified or Ultimate Security. Steps:
[student-follow-docs/enable-catalog-curation.md](student-follow-docs/enable-catalog-curation.md).
