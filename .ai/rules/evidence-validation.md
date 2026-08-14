# AI Evidence and Validation Rule

## Purpose

Ensure AI engineering decisions and implementation claims are supported by repository evidence.

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

MUST be supported by actual repository inspection or command output.

## Failure

If validation cannot be performed, AI MUST state that validation could not be performed.

AI MUST NOT fabricate successful validation.

## Completion

A task is complete only when:

1. The authorized work exists.
2. Relevant validation has been performed.
3. Known failures are reported.
4. Remaining risks or limitations are explicit.
5. The resulting repository state is understood.
