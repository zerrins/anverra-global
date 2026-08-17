# AI Evidence and Validation Rule

## Purpose

Ensure AI engineering decisions and implementation claims are supported by
repository evidence. Evidence precedes every claim — without exception.

## Evidence

AI MUST distinguish between:

- Observed repository facts
- Documented requirements
- Architectural decisions
- Implementation findings
- Inferences
- Recommendations
- Assumptions

AI MUST NOT present an inference as an established repository fact.

## Evidence Gate

Before making any status claim or declaring any outcome, AI MUST apply the
following gate in order. Skipping any step is not verification — it is assertion.

```
1. IDENTIFY   What command or inspection proves this claim?
2. RUN        Execute that command or inspection now (fresh, complete).
3. READ       Read the full output. Check exit codes. Count failures.
4. VERIFY     Does the actual output confirm the claim?
              → If NO: state the actual status with the observed evidence.
              → If YES: state the claim WITH the evidence.
5. CLAIM      Only then make the claim.
```

## Validation

Before declaring work complete, AI MUST validate the relevant:

- Requirements
- Architecture
- Implementation
- Tests
- Dependencies
- Database changes
- API contracts
- Documentation
- Security constraints

## Verification

Claims such as:

- "implemented"
- "fixed"
- "verified"
- "no contradictions"
- "tests pass"
- "no files changed"
- "build succeeds"
- "linter clean"

MUST be supported by actual repository inspection or command output obtained
in this session, after the work was completed.

The following are NOT sufficient evidence:

| Claim | Requires | Not Sufficient |
|---|---|---|
| Tests pass | Test command output: 0 failures | Previous run, "should pass" |
| Build succeeds | Build command: exit 0 | Linter passing, logs look clean |
| Bug fixed | Test that reproduces original symptom: passes | Code changed, assumed fixed |
| No files changed | `git status` or directory listing output | Memory of what was done |
| Requirements met | Evidence against each requirement | Tests passing alone |

## Prohibited Language Without Evidence

AI MUST NOT use the following expressions without current command output as support:

- "should work"
- "probably passes"
- "seems correct"
- "I believe it works"
- "the tests should pass"

When evidence is unavailable, state explicitly what could not be verified
and why.

## Failure

If validation cannot be performed, AI MUST state that validation could not
be performed and explain the reason.

AI MUST NOT fabricate successful validation.

## Completion

A task is complete only when:

1. The authorized work exists.
2. Relevant validation has been performed using the Evidence Gate above.
3. Known failures are reported.
4. Remaining risks or limitations are explicit.
5. The resulting repository state is understood.
