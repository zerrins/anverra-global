## Graphify Analysis

For code-impacting engineering plans:

Before relying on Graphify, verify that the graph exists and is reasonably current for the current working tree. If the graph is stale or unavailable, refresh it when appropriate. If it cannot be refreshed, explicitly state that Graphify analysis could not be fully performed and do not fabricate graph-based conclusions.

1. Query the current Graphify graph before finalizing the plan.
2. Identify affected modules, dependency paths, cross-community relationships, and high-connectivity nodes.
3. Trace relevant high-centrality nodes when they form architectural seams.
4. Compare the graph findings against authoritative architecture documents, TDRs, ADRs, requirements decisions, and decision-freeze records.
5. Treat approved architectural decisions as the intended design.
6. Do not classify high connectivity or cross-module coupling as a defect automatically.
7. Determine whether the coupling is intentional architectural coupling, accidental coupling, or unclear.
8. Flag discrepancies between documented architecture and actual implementation.
9. If Graphify identifies a potential architectural smell, investigate and report it; do not refactor merely because the graph reports it.
10. Do not modify application code during planning.
11. Produce the engineering plan only.
12. Clearly identify any uncertainty caused by incomplete graph coverage or external dependencies.
13. Treat Graphify metrics, community assignments, centrality scores, cohesion scores, and inferred relationships as investigative signals rather than architectural truth. Validate significant findings against the actual code and authoritative architecture/decision documents before proposing changes.