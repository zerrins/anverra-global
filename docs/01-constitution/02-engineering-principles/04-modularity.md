# Modularity

**Stage:** 2 — Engineering Principles  
**Document:** 04 — Modularity  
**Version:** 1.0  
**Status:** Expanded Draft  
**Principle ID:** AEOS-EP-004

---

# 1. Purpose

This document defines the **Modularity** engineering principle.

Modularity establishes that software should be organized around meaningful responsibilities and business capabilities with clear boundaries and controlled dependencies.

The objective is not to maximize the number of modules.

The objective is to make the system easier to:

- understand
- change
- test
- operate
- evolve
- govern

while minimizing unnecessary change propagation.

---

# 2. Principle Statement

> **Business capabilities should have clear boundaries, controlled responsibilities, and explicit dependencies so that change can remain localized.**

Modularity is therefore primarily about **controlling change**, not about creating more packages, classes, services, or repositories.

---

# 3. Why Modularity Matters

Anverra Global is intended to be a modular, maintainable, scalable enterprise platform.

The existing product principles state that business capabilities shall be developed as independent modules with clear boundaries. :contentReference[oaicite:2]{index=2}

The Engineering Vision further clarifies that modular engineering should preserve meaningful business boundaries and minimize unnecessary change propagation. :contentReference[oaicite:3]{index=3}

Therefore modularity exists primarily to support:

```text
Business Evolution
      ↓
Controlled Change
      ↓
Limited Impact
      ↓
Safer Delivery
      ↓
Long-Term Maintainability
```

---

# 4. Modularity Is About Change

The most useful question when evaluating a boundary is:

> **What happens when this responsibility changes?**

A good boundary should help contain change.

For example:

```text
Customer Management
        │
        ├── Customer
        ├── Customer Rules
        ├── Customer Lifecycle
        └── Customer Interfaces
```

A change to customer-specific behavior should ideally remain primarily within the customer capability.

Poor modularity may produce:

```text
Customer Change
      ↓
Policy
      ↓
Commission
      ↓
Notification
      ↓
Reporting
      ↓
Unrelated Infrastructure
```

where unrelated areas must be modified because responsibilities are tightly coupled.

---

# 5. Business Boundaries Before Technical Boundaries

Modules should primarily reflect meaningful business responsibilities.

Anverra's initial capabilities include:

- Identity
- Customer Management
- Agent Management
- Dealer Management
- Product Catalogue
- Policy Lifecycle
- Document Management
- Commission Management
- Notifications
- Reporting
- Administration

These capabilities provide an important starting point for modular thinking. :contentReference[oaicite:4]{index=4}

The exact technical decomposition belongs to later architecture artifacts.

The principle is:

> **Business concepts should drive meaningful boundaries; frameworks should not arbitrarily define them.**

---

# 6. Domain Before Framework

The existing architectural principles explicitly establish:

1. Domain before framework
2. Explicit dependencies
3. No cyclic module dependencies
4. Infrastructure depends on domain
5. Public contracts only across modules
6. Backward compatibility where practical

:contentReference[oaicite:5]{index=5}

These principles reinforce the modularity objective.

The architecture should therefore not become:

```text
Framework
    ↓
Technical structure
    ↓
Business logic
```

Instead:

```text
Business Domain
      ↓
Module Boundary
      ↓
Application Structure
      ↓
Technology
```

---

# 7. Module Responsibility

Every meaningful module should have an understandable responsibility.

A module should make it reasonably easy to answer:

- What does this module own?
- What business capability does it represent?
- What rules belong here?
- What does it depend on?
- Who consumes it?
- What does it expose?

A module that has no clear answer to these questions should be reconsidered.

---

# 8. Controlled Responsibility

Modularity does not mean that every class becomes a module.

Over-decomposition can create:

- excessive indirection
- difficult navigation
- unnecessary interfaces
- additional configuration
- harder debugging
- higher cognitive load

The existing AEOS value explicitly states:

> **The objective is not maximum decomposition. The objective is controlled responsibility and dependency.** :contentReference[oaicite:6]{index=6}

Therefore:

```text
Too Little Decomposition
        ↓
High Coupling

Too Much Decomposition
        ↓
High Complexity

Meaningful Decomposition
        ↓
Controlled Change
```

---

# 9. Cohesion

A module should contain responsibilities that naturally belong together.

High cohesion generally makes a module:

- easier to understand
- easier to test
- easier to modify
- easier to own

For example:

```text
Policy Module
 ├── Policy
 ├── Policy Rules
 ├── Policy Lifecycle
 └── Policy Validation
```

is generally more meaningful than distributing policy behavior across unrelated technical modules without a clear reason.

---

# 10. Coupling

Modules should avoid unnecessary coupling.

Coupling can occur through:

- direct method calls
- shared mutable state
- shared database structures
- shared internal classes
- hidden configuration
- implicit assumptions
- duplicated business rules

Dependencies should therefore be:

- explicit
- intentional
- understandable
- minimal where practical

---

# 11. Explicit Dependencies

A module should clearly expose what it depends on.

For example:

```text
Policy
   ↓
Product Catalogue
```

is easier to understand than:

```text
Policy
   ↓
Some shared utility
   ↓
Another service
   ↓
Hidden database query
   ↓
Product data
```

Explicit dependencies improve:

- reasoning
- testing
- debugging
- architecture review
- AI-assisted development

---

# 12. Dependency Direction

Dependencies should have an intentional direction.

The architecture should avoid circular relationships such as:

```text
Customer → Policy
   ↑         ↓
   └─────────┘
```

Cyclic dependencies make change and understanding more difficult.

The existing architectural principles explicitly prohibit cyclic module dependencies. :contentReference[oaicite:7]{index=7}

---

# 13. Public Contracts

Modules should communicate through deliberate contracts.

The existing architectural principles establish:

> **Public contracts only across modules.** :contentReference[oaicite:8]{index=8}

This means one module should not depend on another module's internal implementation merely because it is technically accessible.

Prefer:

```text
Module A
   │
   │ Public Contract
   ↓
Module B
```

over:

```text
Module A
   │
   ├── internal class
   ├── internal database object
   ├── internal helper
   └── internal implementation
```

---

# 14. Encapsulation

A module should protect its internal implementation.

Consumers should generally care about:

- what the module does
- what contract it provides
- what inputs it accepts
- what outputs it produces

rather than:

- how the module stores data
- which internal classes it uses
- which internal algorithm it uses

This allows implementation to evolve without unnecessarily affecting consumers.

---

# 15. Modularity and Data Ownership

Data ownership should support module boundaries.

A module should have clear responsibility for the business data it owns.

Avoid situations where many modules freely manipulate the same business state without clear ownership.

For example:

```text
Policy Module
      ↓
Policy State
```

is easier to reason about than:

```text
Customer Module ─┐
Agent Module ────┼──→ Shared Policy State
Commission ──────┤
Reporting ───────┘
```

The precise database architecture belongs to later AEOS architecture artifacts.

---

# 16. Modularity and Database Boundaries

A modular application does not necessarily require one database per module.

The important principle is ownership and controlled access.

A modular monolith can still provide strong boundaries.

The current product direction explicitly identifies **Modular Monolith architecture initially**. :contentReference[oaicite:9]{index=9}

Therefore:

> **Modularity does not require microservices.**

---

# 17. Modularity and the Modular Monolith

A modular monolith can provide:

- clear business boundaries
- controlled dependencies
- independent testing
- lower operational complexity
- simpler deployment
- future extraction options

A useful mental model is:

```text
                    Application
                         │
       ┌─────────────────┼─────────────────┐
       │                 │                 │
   Customer           Policy          Commission
   Module             Module             Module
       │                 │                 │
       └────── Controlled Contracts ───────┘
```

The application may deploy as one unit while retaining logical modular boundaries.

---

# 18. Modularity Does Not Mean Microservices

Microservices introduce additional operational complexity.

Examples include:

- network communication
- deployment coordination
- service discovery
- distributed tracing
- versioning
- failure handling
- infrastructure management

Therefore a business capability should not become a separate service merely because it is a conceptual module.

The existing AEOS principle of simplicity before complexity applies.

---

# 19. Modularity and Change Propagation

A key modularity indicator is change propagation.

Suppose a policy rule changes.

A well-modularized system might require:

```text
Policy Module
 ├── Rule
 ├── Test
 └── Documentation
```

A poorly modularized system might require:

```text
Policy
Customer
Commission
Reporting
Notification
Database
API
Infrastructure
```

The second situation indicates excessive coupling.

---

# 20. Modularity and Testing

Modules should be independently testable where appropriate.

The Engineering Vision explicitly states that modules should be independently testable where appropriate. :contentReference[oaicite:10]{index=10}

Good modular boundaries therefore improve testability.

```text
Clear Module
     ↓
Controlled Dependencies
     ↓
Isolated Test Setup
     ↓
Faster Feedback
```

---

# 21. Modularity and Maintainability

Modularity directly supports maintainability.

The relationship is:

```text
Modularity
    ↓
Controlled Dependencies
    ↓
Limited Change Propagation
    ↓
Lower Cognitive Cost
    ↓
Improved Maintainability
```

Therefore modularity should be evaluated partly by whether it makes future maintenance easier.

---

# 22. Modularity and AI

AI agents need predictable boundaries.

A modular system allows an agent to reason about:

- one capability
- its responsibilities
- its dependencies
- its contracts
- its tests
- its documentation

without requiring the entire repository context for every change.

This aligns with the AEOS context-loading model, which favors loading the minimum relevant context while preserving traceability. :contentReference[oaicite:11]{index=11}

---

# 23. Modularity and Ownership

Every meaningful module should have identifiable ownership.

Ownership may include responsibility for:

- business behavior
- implementation
- tests
- documentation
- operational behavior
- architectural compliance

Ownership should reduce ambiguity about who is responsible for maintaining a capability.

---

# 24. Modularity Anti-Patterns

Avoid:

- modules with unclear responsibilities
- cyclic dependencies
- shared mutable state without ownership
- unrestricted access to internal implementation
- giant shared utility modules
- arbitrary technical decomposition
- microservices introduced without business or operational justification
- duplicated business rules across modules
- modules that exist only because of framework conventions
- excessive cross-module calls
- hidden dependencies

---

# 25. Modularity Review Questions

Before introducing or changing a module, ask:

1. What business capability does it represent?
2. What responsibility does it own?
3. What does it depend on?
4. Who depends on it?
5. Are dependencies explicit?
6. Are internal details protected?
7. Can it be tested appropriately?
8. Will the boundary reduce change propagation?
9. Is the decomposition justified?
10. Is the additional complexity worth the benefit?

---

# 26. Modularity Checklist

- [ ] Business responsibility is clear.
- [ ] Module ownership is clear.
- [ ] Dependencies are explicit.
- [ ] Dependency direction is intentional.
- [ ] Cyclic dependencies are avoided.
- [ ] Public contracts are used across module boundaries.
- [ ] Internal implementation is protected.
- [ ] Business rules are not duplicated unnecessarily.
- [ ] Data ownership is clear.
- [ ] Module can be tested appropriately.
- [ ] Module structure reduces unnecessary change propagation.
- [ ] Decomposition is justified.
- [ ] Microservice extraction is not being introduced without sufficient reason.
- [ ] Documentation reflects important boundaries.

---

# 27. Final Rule

> **Create boundaries where they reduce change impact, not merely where they increase structural separation.**

The goal is not:

> "More modules."

The goal is:

> **"More controlled change."**