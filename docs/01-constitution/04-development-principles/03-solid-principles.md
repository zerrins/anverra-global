---
document: SOLID Principles
id: AEC-DEV-003
version: 1.0.0
status: Draft
stability: Level 3
owner: Engineering
created: 2026-08-03
last-reviewed:
depends-on:
  - AEC-DEV-001
  - AEC-DEV-002
  - AEC-ARC-002
  - AEC-ARC-004
---

# Purpose

Establish how the SOLID principles shall be applied within the Anverra Global platform.

SOLID is adopted as a design guideline that improves maintainability, extensibility, and testability. It is not treated as a set of rigid rules.

The objective is to produce software that evolves gracefully while avoiding unnecessary abstraction.

---

# Intent

SOLID exists to help engineers design software that is:

- Easy to understand
- Easy to modify
- Easy to extend
- Easy to test
- Resistant to unintended side effects

The principles are tools for improving software quality—not goals in themselves.

---

# Problem Statement

Poor software design often results in:

- Large classes with many responsibilities
- Tight coupling
- Rigid implementations
- Difficult testing
- Duplicate behavior
- Fragile changes
- Excessive inheritance
- Unnecessary abstractions

Conversely, over-applying SOLID can also lead to:

- Interface explosion
- Abstraction for its own sake
- Excessive indirection
- Difficult navigation
- Premature extensibility

Both extremes reduce maintainability.

---

# Development Decision

Anverra Global adopts SOLID as a design framework.

SOLID shall always support business clarity.

Business simplicity has precedence over theoretical purity.

The architecture remains:

- Domain Driven
- Hexagonal
- Modular

SOLID strengthens these architectural principles.

It never replaces them.

---

# Rationale

Most software spends significantly longer being maintained than initially developed.

Well-designed software:

- changes safely,
- scales incrementally,
- reduces regression defects,
- improves AI code generation,
- improves engineering productivity.

SOLID helps achieve these outcomes when applied pragmatically.

---

# Why This Matters to AI

AI models naturally generate correct implementations but often introduce unnecessary abstractions.

Without guidance, AI may:

- create interfaces for every class,
- split cohesive behavior unnecessarily,
- overuse inheritance,
- violate business cohesion,
- optimize for textbook examples instead of production systems.

This document teaches AI to balance design quality with practical engineering.

---

# Principle 1 — Single Responsibility Principle (SRP)

## Definition

Every class, component, or module should have one primary reason to change.

Responsibility is determined by business behavior—not by method count.

---

### Mandatory Rules

A class shall represent one cohesive concept.

Business responsibilities shall not be mixed.

Technical responsibilities shall remain separate.

---

### Recommended Practices

Keep classes cohesive.

Extract unrelated responsibilities.

Model business concepts explicitly.

---

### Avoid

Splitting cohesive business behavior merely to satisfy SRP mechanically.

---

# Principle 2 — Open/Closed Principle (OCP)

## Definition

Software should be open for extension while closed for unnecessary modification.

---

### Mandatory Rules

Stable business behavior should be extensible.

Avoid modifying proven implementations without necessity.

---

### Recommended Practices

Use composition.

Use strategy patterns where variation is expected.

Use polymorphism only when justified.

---

### Avoid

Creating extension points before a real need exists.

---

# Principle 3 — Liskov Substitution Principle (LSP)

## Definition

Derived implementations shall preserve the behavioral expectations of their parent abstractions.

---

### Mandatory Rules

Subtypes must behave consistently.

Contracts shall not be weakened.

Behavior shall remain predictable.

---

### Recommended Practices

Favor interfaces representing business capabilities.

Prefer composition when inheritance changes behavior.

---

### Avoid

Inheritance solely for code reuse.

---

# Principle 4 — Interface Segregation Principle (ISP)

## Definition

Consumers should depend only upon the operations they actually require.

---

### Mandatory Rules

Interfaces shall remain cohesive.

Avoid large "everything" interfaces.

---

### Recommended Practices

Expose focused business capabilities.

Keep contracts minimal.

---

### Avoid

Creating interfaces containing unrelated operations.

---

# Principle 5 — Dependency Inversion Principle (DIP)

## Definition

Business logic depends upon abstractions rather than implementations.

---

### Mandatory Rules

The Domain depends on abstractions.

Infrastructure implements abstractions.

Dependencies point inward.

---

### Recommended Practices

Use dependency injection.

Keep abstractions business oriented.

Treat frameworks as implementation details.

---

### Avoid

Depending directly on framework implementations.

---

# Applying SOLID within AEOS

SOLID is applied differently across architectural layers.

## Domain

Strong adherence.

Rich domain models.

Business cohesion.

Minimal technical dependencies.

---

## Application

Focus on orchestration.

Avoid unnecessary abstractions.

---

## Adapter

Keep implementations thin.

Avoid business decisions.

---

## Infrastructure

Technical implementations.

Framework-specific code remains isolated.

---

# Mandatory Rules

Apply SOLID only when it improves maintainability.

Do not sacrifice readability.

Do not increase complexity unnecessarily.

Business cohesion has precedence.

Architecture has precedence over SOLID.

---

# Recommended Practices

Prefer composition.

Prefer explicit behavior.

Use interfaces where multiple implementations are realistic.

Extract abstractions only after duplication indicates stability.

Refactor continuously.

---

# Prohibited Practices

Do not create interfaces for every class.

Do not inherit for convenience.

Do not split cohesive business behavior.

Do not create abstraction layers with only one foreseeable implementation.

Do not introduce unnecessary design patterns.

---

# Allowed Exceptions

Simple utility classes may not require interfaces.

Reference data services may remain concrete.

Performance-critical implementations may optimize object structure when justified.

Exceptions shall be documented.

---

# AI Guidance

Before introducing an abstraction, AI shall ask:

- Does this reduce coupling?
- Does this improve maintainability?
- Is another implementation realistically expected?
- Does the abstraction represent a business capability?
- Does this simplify future evolution?

If the answer is no, AI shall avoid creating the abstraction.

AI shall optimize for business clarity rather than textbook SOLID compliance.

---

# Implementation Guidance

When implementing new functionality:

1. Start with the simplest correct implementation.
2. Ensure responsibilities are cohesive.
3. Evaluate dependency direction.
4. Introduce abstractions only when justified.
5. Validate against architectural principles.
6. Refactor after understanding the business behavior.

SOLID should emerge naturally from good design rather than being imposed mechanically.

---

# Review Checklist

Reviewers shall verify:

- Does each class have one primary responsibility?
- Are abstractions justified?
- Is inheritance appropriate?
- Are interfaces cohesive?
- Does business logic depend only on abstractions?
- Has complexity been reduced?
- Has unnecessary indirection been introduced?
- Does the implementation remain readable?

---

# Examples

## Good

CustomerRepository (interface)

↓

JpaCustomerRepository

↓

FutureCosmosCustomerRepository

The abstraction represents a genuine architectural boundary.

---

## Good

CommissionCalculatorStrategy

↓

PercentageCommissionCalculator

↓

TieredCommissionCalculator

Business variation justifies polymorphism.

---

## Bad

CustomerService

↓

CustomerServiceInterface

↓

CustomerServiceImpl

Only one implementation exists and no variation is expected.

This abstraction provides little value.

---

## Bad

Ten inheritance levels to avoid duplicating two methods.

Complexity exceeds benefit.

---

# Anti-patterns

Interface Explosion

Abstract Factory Everywhere

Inheritance for Code Reuse

Pattern-Oriented Programming

Architecture Astronautics

Speculative Generality

Needless Indirection

---

# Engineering Decision

SOLID strengthens architecture.

SOLID never overrides:

- Business requirements
- Domain-Driven Design
- Hexagonal Architecture
- Simplicity

When conflict exists:

Business simplicity wins.

---

# References

- Robert C. Martin — Agile Software Development: Principles, Patterns and Practices
- Martin Fowler — Refactoring
- Steve McConnell — Code Complete

---

# Related Documents

- Development Philosophy
- Clean Code
- Code Readability
- Refactoring
- Domain-Driven Design
- Hexagonal Architecture
- Dependency Direction