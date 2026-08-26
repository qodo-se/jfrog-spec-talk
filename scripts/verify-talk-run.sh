#!/usr/bin/env bash
# Grade the talk exercises against the expected end state:
#   REQ-002 implemented, @REQ-002 scenarios present, chosenPackage in pom.xml.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"

python3 - "$ROOT/requirements/requirements.json" \
    "$ROOT/src/test/resources/features/contacts.feature" \
    "$ROOT/pom.xml" <<'PY'
import json, re, sys

spec_path, feature_path, pom_path = sys.argv[1:4]
fail = 0

def report(ok, label, detail=""):
    global fail
    print(f"  {'PASS' if ok else 'FAIL'}  {label}" + (f" - {detail}" if detail and not ok else ""))
    if not ok:
        fail = 1

with open(spec_path) as f:
    spec = json.load(f)
with open(feature_path) as f:
    feature = f.read()
with open(pom_path) as f:
    pom = f.read()

req = next((r for r in spec["requirements"] if r["id"] == "REQ-002"), None)
report(req is not None and req.get("status") == "implemented",
       "REQ-002 status is 'implemented' in the spec",
       f"status is {req.get('status') if req else 'missing'}")

tagged = [line for line in feature.splitlines() if re.match(r"\s*@REQ-002\b", line)]
report(len(tagged) >= 1,
       "@REQ-002 scenarios exist in the feature file",
       "no @REQ-002 tag found — Exercise 2 appends scenarios")

chosen = (req or {}).get("chosenPackage")
if not chosen or not isinstance(chosen, str) or ":" not in chosen:
    report(False, "chosenPackage artifact is present in pom.xml",
           "chosenPackage missing or not groupId:artifactId — Exercise 1 records it")
else:
    artifact = chosen.split(":")[1]
    in_pom = bool(re.search(rf"<artifactId>\s*{re.escape(artifact)}\s*</artifactId>", pom))
    report(in_pom,
           "chosenPackage artifact is present in pom.xml",
           f"{artifact} not found in pom.xml")

print()
if fail:
    print("Run does NOT match the expected end state - see FAIL lines above.")
    sys.exit(1)
print("Run matches the expected end state.")
sys.exit(0)
PY
