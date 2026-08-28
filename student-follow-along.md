# Student Follow-Along: Spec-Driven Development with JFrog MCP

Your step-by-step companion for the talk. Everything the presenter does,
you do — this page has the exact commands, the exact agent prompts, and
what you should see at every step.

**The big idea:** the Contacts API in this repo is *finished
infrastructure* — you use it, you don't rebuild CRUD. Your hour is
spec-driven design with JFrog MCP in the loop: draft a requirement *with*
an agent, use JFrog MCP before any new Maven library hits the POM, then
drive it spec → Gherkin → RED → GREEN.

JFrog SaaS MCP is OAuth-only. Do not put API keys or identity tokens in
`mcp.json`. Those belong to Artifactory REST and the JFrog CLI, not this
Cursor server.

---

## Before the talk

You need:

- **Java 21+** (`java -version`)
- **Cursor** (or any MCP-capable agent — Claude Desktop works with the same URL)
- This repo cloned

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

Open Cursor's **Settings → MCP** and confirm `jfrog` shows **green**. The
first time, Cursor opens a browser for OAuth against the trial JPD — finish
that login. If it's red: toggle the server off/on in the settings after
OAuth completes.

### Callout — configure with the JFrog CLI instead

If you already use the JFrog CLI and prefer it to write the MCP entry for
you (same URL, still OAuth in the agent afterward):

```bash
jf mcp install --agent cursor --mcp-url=https://trialiqsxt4.jfrog.io/mcp
```

Then approve `jfrog` in **Settings → MCP** and complete OAuth. The CLI
stores no credentials — only the endpoint.

---

## Step 3 — Exercise 1: draft the spec and pick a library (minutes 15–35)

Paste this into Cursor's agent, word for word:

> Draft requirement REQ-002 in requirements/requirements.json: export
> contacts as CSV. Follow the existing format — unique id, title, user
> story, acceptance criteria phrased Given/When/Then, status pending,
> featureFile pointing at src/test/resources/features/contacts.feature.
> Before choosing a Maven library, use JFrog MCP. Record the chosen
> coordinates as chosenPackage (groupId:artifactId) and chosenVersion if
> you pin one. For this hour use org.apache.commons:commons-csv and pin
> 1.14.1 unless I redirect you. Do not write scenarios or production
> code yet — we are only agreeing on the spec and the library.

**What you should see, in order:**

1. The agent rewrites **REQ-002** in
   `requirements/requirements.json` (story + Given/When/Then criteria).
   Read what it wrote — requirements gathering didn't disappear, it moved
   to review.
2. The agent calls **JFrog MCP** (`artifactory_packages_get_versions` for
   Maven `org.apache.commons:commons-csv`). You should see **1.14.1** in
   the version list (it is already stored in this Artifactory). It then
   fills `chosenPackage` / `chosenVersion` — for this hour
   `org.apache.commons:commons-csv` and `1.14.1` unless you redirect.
3. **Your checkpoint:** read the story, the criteria, *and* the chosen
   coordinates aloud. Is this the export we meant, and is this library
   one we would actually ship? You own the intent — approve it or
   redirect the agent with one sentence.

---

## Step 4 — Exercise 2: spec to green (minutes 35–55)

Paste this into Cursor's agent, word for word:

> Using the approved REQ-002 spec: add Gherkin scenarios tagged @REQ-002
> to src/test/resources/features/contacts.feature for its acceptance
> criteria, reuse or add step definitions, add a matching JUnit test in
> ContactApiTest, run ./mvnw test to show RED, then add only the
> dependency recorded as chosenPackage to pom.xml and implement
> the simplest code to reach GREEN, then mark REQ-002 implemented in
> requirements/requirements.json. Ask me before each phase change.

**What you should see, in order:**

1. The agent appends `@REQ-002` scenarios to
   `src/test/resources/features/contacts.feature` and a REQ-002 unit test
   to `ContactApiTest`.
   **Your checkpoint 1:** read the scenario. This is the spec review —
   is this the CSV export you want?
2. `./mvnw test` → **RED** — new scenarios/tests fail because there is
   no export endpoint yet.
3. The agent adds **only** the coordinates from `chosenPackage` to
   `pom.xml` and implements the simplest CSV export that passes.
4. `./mvnw test` → **GREEN**.
5. The agent flips REQ-002 to `"status": "implemented"` in the spec.
   **Your checkpoint 2:** approve the final diff. Two checkpoints, both
   yours — the scenario and the code.

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

The [talk plan](talk-plan.md) continues past GREEN: a PR, Qodo review
against the spec and standards, merge, then build into JFrog Artifactory
with Xray, SBOM, Evidence, and a release bundle. That is the rest of the
platform story — you do not need to run it in this hour.

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
- **Cursor MCP connection red:** toggle `jfrog` off/on in Settings → MCP
  after completing OAuth. Confirm the URL is
  `https://trialiqsxt4.jfrog.io/mcp` with no Authorization header.
- **Agent adds a library without calling JFrog MCP:** stop it. The
  project rule is: no new Maven dependency until JFrog MCP has been
  called. Re-paste Exercise 1.
- **Agent goes sideways:** it happens. Undo its edits, clear the chat, and
  re-paste the prompt — or follow the presenter's fallback on screen.
