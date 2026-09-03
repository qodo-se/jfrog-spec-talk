# Student Follow-Along: Spec-Driven Development with JFrog MCP

Your step-by-step companion for the talk. Everything the presenter does,
you do — this page has the exact commands, the exact agent prompts, and
what you should see at every step.

**The big idea:** the Contacts API in this repo is *finished
infrastructure* — you use it, you don't rebuild CRUD. Your hour is
spec-driven design with JFrog MCP in the loop: draft a requirement *with*
your coding agent, use JFrog MCP before any new Maven library hits the POM, then
drive it spec → Gherkin → RED → GREEN.

JFrog SaaS MCP is OAuth-only. Do not put API keys or identity tokens in
`mcp.json`. Those belong to Artifactory REST and the JFrog CLI, not this
MCP server.

---

## Before the talk

You need:

- **Java 21+** (`java -version`)
- **A coding agent that can host MCP** (Cursor, Claude Desktop, and others
  work with the same JFrog URL)
- This repo cloned
- **Qodo, for the review phase at the end of Step 4.** Install
  the skill and log the CLI in:

  ```bash
  curl -fsSL https://get.qodo.ai | sh
  qodo login
  ```

  Without it, Step 4 still runs spec → RED → GREEN; your agent will just
  report the review as skipped.

The Maven Wrapper is in the repo (`./mvnw`) — you do not need a global Maven
install. Build once at home so the room's Wi-Fi never matters:

```bash
./mvnw -q package
```

Because of `-q` (quiet), Maven prints no download or compile chatter — the
Cucumber scenario narration from the test run starts like this:

```text
@REQ-001
Scenario: A new contact can be created                                           # features/contacts.feature:14
  Given the contacts API is running                                              # com.davidparry.jfrog.jfrogspectalk.contact.ContactSteps.theContactsApiIsRunning()
```

…and continues through every `@REQ-001` scenario. Compare yours against
the full captured run:
[student-follow-docs/pre-step.log](student-follow-docs/pre-step.log).
A Mockito / Java-agent warning in the captured run is expected — it is
not a test failure. The build is good when the command exits without a
`BUILD FAILURE` banner — check with `echo $?` right after; `0` means
success.

---

## Step 1 — Branch, then build (first 5 minutes)

Never work on the default branch — the exercises rewrite the spec and the
tests, and the starting branch must stay pristine so you can always reset
by re-branching. From the repo root:

```bash
git checkout -b talk
./mvnw -q package
```

**Expect:** a green build. If it's red, raise a hand and pair with a
neighbor — don't fall behind debugging alone.

---

## Step 2 — Connect JFrog MCP

This repo already registers the server in [`.cursor/mcp.json`](.cursor/mcp.json)
(and [`.mcp.json`](.mcp.json) for Claude-style clients):

```json
{
  "mcpServers": {
    "jfrog": {
      "url": "https://trialiqsxt4.jfrog.io/mcp"
    }
  }
}
```

Open your coding agent's MCP settings and confirm `jfrog` shows **green**.
In Cursor that is **Settings → MCP**. The first time, the host opens a
browser for OAuth against the trial JPD — finish that login. If it's red:
toggle the server off/on in the settings after OAuth completes.

### Callout — configure with the JFrog CLI instead

If you already use the JFrog CLI and prefer it to write the MCP entry for
you (same URL, still OAuth in your coding agent afterward):

```bash
jf mcp install --agent cursor --mcp-url=https://trialiqsxt4.jfrog.io/mcp
```

Then approve `jfrog` in your coding agent's MCP settings and complete
OAuth. The CLI stores no credentials — only the endpoint. The `--agent
cursor` flag is the Cursor example; use the CLI's agent name for yours.

---

## Step 3 — Exercise 1: draft the spec and pick a library (minutes 15–35)

Paste this into your coding agent, word for word:

> Draft REQ-002 in requirements/requirements.json for exporting contacts as CSV. Match REQ-001's format (story, Given/When/Then criteria, status pending). Use JFrog MCP to pick the Maven dependency and set `chosenPackage` and `chosenVersion` from the version it lists — do not invent a version. Spec and dependency only: no Gherkin, tests, or production code.

**What you should see, in order:**

1. Your coding agent rewrites **REQ-002** in
   `requirements/requirements.json` (story + Given/When/Then criteria).
   Read what it wrote — requirements gathering didn't disappear, it moved
   to review.
2. Your coding agent calls **JFrog MCP**
   (`artifactory_packages_get_versions` for Maven
   `org.apache.commons:commons-csv`). You should see **1.14.1** in
   the version list (it is already stored in this Artifactory). It then
   fills `chosenPackage` / `chosenVersion` — for this hour
   `org.apache.commons:commons-csv` and `1.14.1` unless you redirect.
3. **Your checkpoint:** read the story, the criteria, *and* the chosen
   coordinates aloud. Is this the export we meant, and is this library
   one we would actually ship? You own the intent — approve it or
   redirect your coding agent with one sentence.

---

## Step 4 — Exercise 2: spec to green (minutes 35–55)

Paste this into your coding agent, word for word:

> Using the approved REQ-002 spec: add Gherkin scenarios tagged @REQ-002
> to src/test/resources/features/contacts.feature for its acceptance
> criteria, reuse or add step definitions, add a matching JUnit test in
> ContactApiTest, run ./mvnw test to show RED, then add only the
> dependency recorded as chosenPackage to pom.xml and implement
> the simplest code to reach GREEN, then mark REQ-002 implemented in
> requirements/requirements.json, then run the qodo-review skill on the
> local diff with REQ-002's story and acceptance criteria as the session
> context and show me the findings before you change any code. Ask me
> before each phase change.

**What you should see, in order:**

1. Your coding agent appends `@REQ-002` scenarios to
   `src/test/resources/features/contacts.feature` and a REQ-002 unit test
   to `ContactApiTest`.
   **Your checkpoint 1:** read the scenario. This is the spec review —
   is this the CSV export you want?
2. `./mvnw test` → **RED** — new scenarios/tests fail because there is
   no export endpoint yet.
3. Your coding agent adds **only** the coordinates from `chosenPackage` to
   `pom.xml` and implements the simplest CSV export that passes.
4. `./mvnw test` → **GREEN**.
5. Your coding agent flips REQ-002 to `"status": "implemented"` in the spec.
   **Your checkpoint 2:** approve the diff.
6. Your coding agent runs the **`qodo-review`** skill — `qodo review` over
   your uncommitted diff, carrying REQ-002's story and criteria as
   context, so the review judges the code against the spec you approved
   and not against a guess. Expect a minute or two of progress lines,
   then findings tagged `[category/level]`. Nothing is committed, pushed,
   or turned into a PR.
   **Your checkpoint 3:** you decide which findings to apply — your agent
   must ask before it edits. Three checkpoints, all yours: the scenario,
   the code, and the review.

If you skipped the optional Qodo setup in **Before the talk**, your agent
should say the review was skipped in one line and stop — that is the
correct behavior, not a failure. Steps 1–5 of this list still count as a
complete run. `qodo review` diffs your `talk` branch against
`origin/main`, which is already pushed in the clone, so there is nothing
to push first.

---

## Step 5 — Check your work

The repo can grade your run (it **fails on purpose** before the exercises —
REQ-002 is still pending):

```bash
scripts/verify-talk-run.sh
```

**Expect all three PASS:**

```text
  PASS  REQ-002 status is 'implemented' in the spec
  PASS  @REQ-002 scenarios exist in the feature file
  PASS  chosenPackage artifact is present in pom.xml
```

Any FAIL line tells you exactly which artifact to revisit.

---

## After the PR (presenter narrative, not a live exercise)

Step 4 ends with the *pre-PR* review of your local diff. The
[talk plan](talk-plan.md) continues past that: a PR, the same Qodo review
against the spec and standards on every pushed commit, merge, then build
into JFrog Artifactory with Xray, SBOM, Evidence, and a release bundle.
That is the rest of the platform story — you do not need to run it in
this hour.

---

## Reset / start over

Everything the exercises touch lives in `requirements/`, the feature file,
`ContactApiTest`, `ContactSteps`, and `pom.xml`:

```bash
git checkout -- requirements src/test pom.xml src/main
```

or throw the branch away and re-cut it:

```bash
git checkout - && git branch -D talk && git checkout -b talk
```

(`git checkout -` returns to the branch you were on before `talk`.)

---

## If you get stuck

- **Build red:** pair with a neighbor first; the presenter won't debug from
  stage.
- **MCP connection red:** toggle `jfrog` off/on in your coding agent's
  MCP settings (Cursor: **Settings → MCP**) after completing OAuth.
  Confirm the URL is `https://trialiqsxt4.jfrog.io/mcp` with no
  Authorization header.
- **Coding agent adds a library without calling JFrog MCP:** stop it. The
  project rule is: no new Maven dependency until JFrog MCP has been
  called. Re-paste Exercise 1.
- **Coding agent goes sideways:** it happens. Undo its edits, clear the
  chat, and re-paste the prompt — or follow the presenter's fallback on
  screen.
