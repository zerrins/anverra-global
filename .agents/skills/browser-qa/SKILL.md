---
name: browser-qa
description: Browser Visual QA capability using Playwright for validation, visual regression, and debugging.
---

# Browser Visual QA

## Purpose
Browser QA validates the ACTUAL RENDERED UI in a real browser using Playwright. 

This skill explains how AnverraGlobal uses Playwright for:
- real browser validation
- responsive testing (375px, 768px, 1024px, 1440px)
- visual regression
- browser console error detection
- network failure detection
- screenshot capture
- reading visual diffs
- reading Playwright reports
- reading traces
- debugging browser failures

## Rule of Evidence
The browser-qa skill must strictly follow the Evidence Rule (IDENTIFY → RUN → READ → VERIFY → CLAIM).
"The page looks correct" is **NOT acceptable evidence**.
Valid evidence includes:
- Playwright test output
- screenshot comparison result
- trace inspection
- browser console output
- network result
- explicit human visual approval

## Scope and Boundaries
Browser QA validates the rendered application.
- It does **NOT** authorize implementation.
- It does **NOT** automatically update screenshots.
- It does **NOT** decide whether a visual difference is correct.

## Visual Baseline Governance
Baseline updates MUST require explicit intent. Do not automatically run `--update-snapshots` after a failed test.

Approved Design
    ↓
Implementation
    ↓
Playwright screenshot
    ↓
Compare with approved baseline
    ↓
PASS / DIFF
    ↓
Human/Engineering decision
    ↓
Fix or explicitly approve baseline update
    ↓
Verify

A visual difference must be:
1. observed
2. investigated
3. classified as intentional or regression
4. corrected or explicitly approved
5. verified again

## Future Baseline Location
Future snapshots will likely reside in `e2e/snapshots/`, though the exact baseline strategy may be finalized during the UI revamp workflow.

## Accessibility
Automated accessibility testing (e.g., via `@axe-core/playwright`) is a planned future capability. Do not claim that Playwright itself provides WCAG validation.

## .ai Integration
This skill integrates with the existing `.ai/skills/testing-validation/` workflow. It adds real browser validation as an additive layer of evidence gathering, while the existing `.ai` system remains authoritative.
