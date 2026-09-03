# Talk Plan

**Students:** follow [`student-follow-along.md`](student-follow-along.md).
They paste the exercise prompts into **their coding agent** (Cursor,
Claude Desktop, or any MCP host with the same JFrog URL). This page is
the presenter map, including what happens *after* GREEN.

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
     ┌────────────┴────────────┐
     │                         │
 Skills/rules             JFrog MCP
 (how to work)          (live tools)
     │                         │
     │                         ├──────► Artifactory versions
     └────────────┬────────────┘
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

The hour on stage stops one phase past GREEN: Exercise 2 ends with a Qodo
**pre-PR review of the local diff** (the `qodo-review` skill), so the room
sees spec-compliance review as part of the loop rather than something that
happens later. Students who skipped the Qodo setup get a one-line "review
skipped" and are still done. The PR, merge, Artifactory, Xray, SBOM,
Evidence, and the release bundle stay narrative — not live lab steps.

## MCP and skills (three slides after the overview)

The coding agent has **no world model** of this JPD, this spec, or these
tests. Token sampling is non-deterministic; this development environment
is not. MCP and skills are the grounded truth it must use instead of
the chat.

**One line:** MCP is what the agent can *do* against the real system;
skills are the instructions it *must follow*. Both are deterministic;
the model is not.

Deck order after About Me: three altitudes → spec loop → this hour →
**these three** → Agenda.

| Slide | Title | Say |
| --- | --- | --- |
| MCP | MCP: live tools into the real system | Model Context Protocol — the coding agent calls real tools. This hour JFrog MCP lists versions **already in this Artifactory**, not Maven Central in the model's head. OAuth only; never API keys in `mcp.json`. |
| Skills | Skills: grounded instructions | A skill is a versioned playbook (`SKILL.md`) — same job as project rules: how we work in *this* repo. Spec first, JFrog before the POM, then `./mvnw test`. Skills do not replace MCP; they say when and how to use it. Point at [`.cursor/rules/jfrog-spec-driven.mdc`](.cursor/rules/jfrog-spec-driven.mdc). |
| Why | Deterministic truth vs a model with no world | MCP = facts and actions from the system. Skills = instructions you wrote. Together they pin spec → library → RED → GREEN. |

Use the **native Google Slides** copy
([Talks deck](https://docs.google.com/presentation/d/1CS07df5yBu8JsDjcdDVisg85_lwbTTsEPlLXtgyjjRI/edit)),
not the yellow-badge `.pptx`. **Extensions → Apps Script**, replace the
file with [`scripts/add-mcp-skills-slides.js`](scripts/add-mcp-skills-slides.js),
**Run** `dumpSlideTitles` (View → Logs — you should see the three overview
titles), then **Run** `addMcpSkillsSlides`. It duplicates the last overview
slide (title + one body box) and writes every bullet into that body. Delete
any extra Thank You copies left by the previous run.
