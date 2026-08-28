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
                  ├──────────────► Artifactory versions
                  │               (commons-csv already in this JPD)
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

**Laptop / JPD prep (not a student paste):** Exercise 1 calls
`artifactory_packages_get_versions`, which lists packages **already stored
in this Artifactory**, not Maven Central. Warm `commons-csv` `1.14.1`
once so the live call returns a version list:

1. In the Platform UI, open **Artifactory → Artifacts**.
2. Open `spectalk-maven-central-remote` and download both files (an
   authenticated GET fills the remote cache):
   - `org/apache/commons/commons-csv/1.14.1/commons-csv-1.14.1.pom`
   - `org/apache/commons/commons-csv/1.14.1/commons-csv-1.14.1.jar`
3. Copy that folder into `spectalk-libs-release-local` so a local repo
   holds the same coordinates.
4. If MCP still returns 0 versions, the files are in storage but Packages
   has not indexed them. Reindex metadata (identity token from Set Me Up):

   ```bash
   curl -X POST -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d '{"paths":["spectalk-libs-release-local","spectalk-maven-central-remote"]}' \
     "https://trialiqsxt4.jfrog.io/artifactory/api/metadata_server/reindex"
   ```

5. Confirm with MCP: `artifactory_packages_get_versions` / maven /
   `org.apache.commons:commons-csv` includes `1.14.1`.
