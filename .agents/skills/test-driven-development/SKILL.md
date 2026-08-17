---
name: test-driven-development
description: "Use when implementing any behavior-changing production code where automated tests are applicable. Write the test first. Watch it fail. Write minimal code to pass. Applies inside .ai/skills/implementation-execution for applicable tasks."
source: https://github.com/obra/superpowers (adapted for AnverraGlobal)
---

# Test-Driven Development (TDD)

## Overview

Write the test first. Watch it fail. Write minimal code to pass.

**Core principle:** If you did not watch the test fail, you do not know if it
tests the right thing.

This skill governs **HOW** applicable production behavior is developed. The
`.ai/skills/implementation-planning` skill remains authoritative for WHAT must
be implemented. TDD applies during `.ai/skills/implementation-execution`.

---

## When TDD Is Mandatory

TDD is mandatory for **behavior-changing production code** where automated
tests are applicable:

- New behavioral features
- Bug fixes (write a test that reproduces the bug first)
- Behavioral refactoring (behavior changes, not structural cleanup)
- New service methods, domain logic, use cases, repository implementations
- New API endpoints or changes to existing endpoint behavior
- New React components with business logic or meaningful state behavior
- Security and authorization behavior

## When TDD Does NOT Apply

Do not force a RED/GREEN/REFACTOR cycle for work where automated behavioral
tests are not meaningful:

- Documentation-only changes
- Static assets (images, fonts, CSS-only visual styling)
- Pure formatting or import reordering
- Trivial non-behavioral refactoring (renaming a variable in an obvious way)
- Design exploration / visual-only UI iteration where visual browser
  verification is the appropriate validation mechanism
- Configuration files with no behavioral logic
- Throwaway spike code (explicitly labeled as such)

When in doubt about applicability, ask — do not assume TDD is not needed.

---

## The Iron Law

```
NO PRODUCTION CODE WITHOUT A FAILING TEST FIRST
```

Wrote code before the test? Delete it. Start over.

- Do not keep it as "reference"
- Do not "adapt" it while writing tests
- Delete means delete

---

## Red-Green-Refactor

### RED — Write a Failing Test

Write one minimal test showing what should happen.

Characteristics of a good test:

- One behavior per test
- Clear, descriptive name that states what the test proves
- Tests real behavior — not implementation internals
- Minimal setup; no unnecessary mocking

### Verify RED — Watch It Fail (Mandatory — Never Skip)

Run the test and confirm:

- Test fails (not errors out with a compilation or import problem)
- Failure message is meaningful and expected
- The test fails because the feature is missing, not due to a typo

**Test passes immediately?** You are testing existing behavior. Fix the test.
**Test errors?** Fix the error, re-run until it fails correctly.

### GREEN — Minimal Code

Write the simplest code that makes the test pass.

- No features beyond what the test requires
- No speculative abstractions
- No "flexibility" that was not asked for

### Verify GREEN — Watch It Pass (Mandatory)

Run the full test suite for the affected module and confirm:

- The new test passes
- Previously passing tests still pass
- No unexpected output, warnings, or errors

### REFACTOR — Clean Up

After GREEN, clean up with confidence:

- Eliminate duplication
- Clarify intent
- Improve naming

**Rules:**

- Tests must remain GREEN throughout refactoring
- Refactor only the code you changed in this task
- Match existing project style and conventions
- Do not add abstractions not needed by any current test

---

## Project-Specific Commands

### Backend — Java 21 / Spring Boot 3.3 / Maven / JUnit 5

**Run a single test class:**
```bash
cd backend
./mvnw test -pl . -Dtest=YourTestClassName
```

**Run tests for a specific package:**
```bash
cd backend
./mvnw test -pl . -Dtest="com.anverraglobal.module.**"
```

**Run all tests:**
```bash
cd backend
./mvnw test
```

**Spring Boot Modular test (Spring Modulith module compliance):**
```java
@ApplicationModuleTest
class SomeModuleTests {
    // Spring Modulith verifies module boundaries automatically
}
```

**ArchUnit architectural boundary test:**
```java
@AnalyzeClasses(packages = "com.anverraglobal")
class ArchitectureTests {
    @ArchTest
    static final ArchRule noCrossModuleDependencies = ...;
}
```

**Testcontainers PostgreSQL integration test:**
```java
@SpringBootTest
@Testcontainers
class SomeRepositoryTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
```

**Layer isolation test (no Spring context — pure unit):**
```java
@ExtendWith(MockitoExtension.class)
class SomeDomainServiceTest {
    @Mock
    private SomePort somePort;

    @InjectMocks
    private SomeDomainService underTest;
}
```

### Frontend — React 19 / TypeScript / Vitest / Testing Library / MSW

**Run tests (non-watch mode):**
```bash
cd frontend
npm run test
# or directly:
npx vitest run
```

**Run a specific test file:**
```bash
cd frontend
npx vitest run src/path/to/YourComponent.test.tsx
```

**Watch mode during development:**
```bash
cd frontend
npm run test:watch
```

**Component test example (Testing Library):**
```tsx
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { YourComponent } from './YourComponent';

test('displays error when field is empty on submit', async () => {
  const user = userEvent.setup();
  render(<YourComponent />);

  await user.click(screen.getByRole('button', { name: /submit/i }));

  expect(screen.getByText(/field is required/i)).toBeInTheDocument();
});
```

**API interaction test (MSW):**
```tsx
import { http, HttpResponse } from 'msw';
import { server } from '../test/server'; // MSW server setup in src/test/

test('shows data after successful fetch', async () => {
  server.use(
    http.get('/api/resource', () => {
      return HttpResponse.json({ id: 1, name: 'Test' });
    })
  );

  render(<YourComponent />);

  expect(await screen.findByText('Test')).toBeInTheDocument();
});
```

---

## Common Failures — Recognize and Avoid

| Mistake | Why it violates TDD |
|---|---|
| Writing implementation code first, then retrofitting tests | Violates the Iron Law. Delete the code. Start over. |
| Testing implementation internals (calling private methods) | Tests the wrong thing. Test behavior via public API. |
| Skipping "watch it fail" because the test "obviously" fails | You don't know if it tests the right thing until you see the failure message. |
| Mocking everything so no real code runs | Not testing anything meaningful. Test real behavior. |
| Not running the full suite after GREEN | Regressions remain undetected. |
| Adding more code than needed to pass the test | Over-engineering. Write just enough for the test to pass. |
| Refactoring across unrelated files during the cycle | Surgical changes only. Refactor only what you changed. |

---

## TDD and the .ai/ Engineering System

TDD applies **inside** `.ai/skills/implementation-execution`. It does not
replace the approved implementation plan — it enforces the HOW of writing each
approved task.

```
.ai/skills/implementation-planning (defines WHAT and WHERE)
    ↓
.ai/skills/implementation-execution (authorized execution)
    ↓ [for each behavior-changing task]
    RED → Verify RED → GREEN → Verify GREEN → REFACTOR → Verify GREEN
    ↓
.ai/skills/testing-validation (post-task validation against all contracts)
```
