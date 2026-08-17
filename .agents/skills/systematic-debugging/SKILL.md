---
name: systematic-debugging
description: "Use when encountering any bug, test failure, or unexpected behavior, before proposing fixes. Root-cause investigation is mandatory before any fix attempt. Applies to both the Java/Spring Boot backend and the React/TypeScript frontend."
source: https://github.com/obra/superpowers (adapted for AnverraGlobal)
---

# Systematic Debugging

## Overview

**Core principle:** ALWAYS find the root cause before proposing any fix.
Symptom fixes are failure — they hide the real problem and guarantee recurrence.

---

## The Iron Law

```
NO FIXES WITHOUT ROOT CAUSE INVESTIGATION FIRST
```

If you have not completed Phase 1, you cannot propose fixes.

---

## When to Use

Use for ANY technical issue:

- JUnit test failures (backend)
- Vitest test failures (frontend)
- Spring Boot startup failures or runtime exceptions
- PostgreSQL / Flyway / data access errors
- React rendering errors or unexpected component behavior
- TypeScript compilation or type errors
- Maven build failures
- Vite build failures
- API contract mismatches (OpenAPI / Orval / MSW)
- ArchUnit / Spring Modulith module boundary violations
- Authentication / OAuth2 / JWT failures
- Integration test failures (Testcontainers)
- Performance problems or memory issues

**Use this ESPECIALLY when:**

- Under time pressure (emergencies make guessing tempting)
- "Just one quick fix" seems obvious
- You have already tried multiple fixes and they didn't work
- You do not fully understand the issue

**Do not skip when:**

- Issue seems simple (simple bugs have root causes too)
- You are in a hurry (systematic is faster than thrashing)

---

## The Four Phases

Complete each phase before proceeding to the next.

---

### Phase 1: Root Cause Investigation

**BEFORE attempting any fix.**

#### 1. Read Error Messages Completely

- Do not skim past stack traces
- Read every line of a stack trace — the actual cause is often several frames down
- Note line numbers, class names, Spring bean names, SQL state codes
- Check suppressed exceptions (Java) and nested causes

**Backend — reading a Spring Boot stack trace:**
```
# Look for the ROOT CAUSE section:
Caused by: org.postgresql.util.PSQLException: ...
    at com.anverraglobal.module.policy.PolicyRepository.save(...)
    # This is where the actual problem is, not the top of the stack
```

**Frontend — reading a Vitest failure:**
```
# Read the diff exactly:
Expected: "ACTIVE"
Received: undefined
# The component received undefined — find where it's set
```

#### 2. Reproduce Consistently

- Can you trigger the failure reliably?
- What are the exact steps or test inputs?
- Does it happen every time?
- Is it environment-specific? (e.g., only with Testcontainers, only in CI, only with a real DB)

If you cannot reproduce it: gather more data first. Do not guess.

#### 3. Check Recent Changes

Before anything else:

```bash
git diff HEAD~1           # What changed in the last commit?
git log --oneline -10     # Recent commit history
git status                # Any uncommitted changes?
```

Ask: what changed that could cause this?

#### 4. Gather Evidence in Multi-Component Systems

AnverraGlobal is a layered system (API → Application Service → Domain → Repository → PostgreSQL). When the failure is in a layer, add diagnostic instrumentation to establish WHERE it breaks before guessing WHY.

**Backend — layer-by-layer instrumentation pattern:**
```java
// Add temporary logging at each boundary:
log.debug("PolicyService.activate called with policyId={}", policyId);
// ... service logic ...
log.debug("PolicyRepository.findById returned: {}", found);
// ... repository result ...
log.debug("Sending to DB: {}", sql);
```

**Check Spring Security context propagation:**
```java
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
log.debug("Auth in context: principal={}, authorities={}", 
    auth.getPrincipal(), auth.getAuthorities());
```

**Check Flyway migration state:**
```bash
./mvnw flyway:info
# Or from Spring Boot logs at startup:
grep "Flyway" application.log
```

**Frontend — check what the component actually receives:**
```tsx
// Add to the component temporarily:
console.debug('YourComponent props:', props);
console.debug('useQuery data:', queryData);
// Or in tests:
screen.debug(); // Prints the current DOM
```

**Check MSW handler registration:**
```tsx
// Log which handlers are active:
server.events.on('request:start', ({ request }) => {
  console.log('MSW intercepted:', request.method, request.url);
});
```

#### 5. Trace Data Flow (see companion: `root-cause-tracing.md`)

When the error is deep in the call stack:

- Where does the bad value originate?
- What passed this bad value to the failing code?
- Keep tracing up the call chain until you find the source
- Fix at the source, not at the symptom

---

### Phase 2: Pattern Analysis

**Find the pattern before fixing.**

1. **Find working examples** — locate similar working code in this codebase. What is the difference?

2. **Compare implementations** — if implementing a Spring Modulith module integration,
   read an existing module's implementation completely. Do not skim.

3. **Identify every difference** — list every difference between working and broken,
   however small. Do not assume "that can't matter."

4. **Check dependencies and configuration:**

   - Spring Boot auto-configuration exclusions
   - `application.properties` / `application-test.properties` differences
   - Testcontainers dynamic property registration
   - Auth0 JWT audience / issuer configuration
   - VITE environment variables in test context (`VITE_API_BASE_URL` etc.)
   - Orval-generated types vs. actual OpenAPI schema

---

### Phase 3: Hypothesis and Testing

**Scientific method — one hypothesis at a time.**

1. **State the hypothesis clearly:**
   > "I believe the failure is caused by X because Y, which means fixing Z should resolve it."

2. **Make the hypothesis falsifiable** — what evidence would prove it wrong?

3. **Test one change at a time** — do not change multiple things simultaneously.

4. **Run the full test suite after each change**, not just the failing test:
   ```bash
   # Backend
   ./mvnw test
   
   # Frontend
   npm run test
   ```

5. **If the hypothesis is wrong** — revert the change. State why the hypothesis
   was wrong. Form a new hypothesis. Do not accumulate guesses.

---

### Phase 4: Fix and Verify

**Fix at the root cause. Verify completely.**

1. Apply the minimal change that addresses the root cause.

2. Run the full test suite:
   ```bash
   # Backend — all tests including integration
   cd backend && ./mvnw test
   
   # Frontend — all tests
   cd frontend && npm run test
   
   # Backend — architecture/module boundary tests
   cd backend && ./mvnw test -Dtest="*ArchTest*,*ModularityTest*"
   ```

3. Verify the original symptom is resolved.

4. Verify no regressions were introduced.

5. Remove all diagnostic instrumentation (temporary logs, `console.debug`, `screen.debug()`).

6. Report the root cause and fix clearly before claiming the issue is resolved.

The AnverraGlobal `.ai/rules/evidence-validation.md` rule applies:
**evidence before claims, always.**

---

## Emergency Protocol

When under extreme time pressure, this abridged sequence is the minimum acceptable:

```
1. READ the full error message and stack trace (2 min)
2. CHECK git diff — what changed? (1 min)
3. REPRODUCE the failure with a single command (2 min)
4. FORM one hypothesis — state it out loud (1 min)
5. APPLY minimal fix targeting the hypothesis
6. RUN the relevant test suite
7. REPORT root cause + evidence
```

Even in an emergency, do NOT skip step 3 (reproduce) or step 7 (report evidence).
Skipping these is how fixes hide deeper problems.

---

## Common Anti-Patterns

| Anti-Pattern | Why It Fails |
|---|---|
| "Let me just try this change and see what happens" | Guessing without a hypothesis wastes time and can introduce new bugs. |
| Fixing the error message location rather than the source | Symptoms re-appear in a different form. |
| Changing multiple things at once | You can't know which change fixed it. |
| Claiming "fixed" without running the full test suite | Regressions go undetected. |
| Adding more error handling to mask the problem | Hides the root cause; guarantees future surprises. |
| Deleting the failing test | The test was telling you something true. |
| "The test is wrong" without evidence | Sometimes true, but always verify first. |

---

## Companion Resource

See `root-cause-tracing.md` (in this skill directory) for the detailed
backward call-chain tracing technique when a bug is deep in the stack.
