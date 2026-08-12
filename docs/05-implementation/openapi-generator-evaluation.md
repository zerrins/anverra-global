# OpenAPI Generator Selection (Implementation-Level Decision)

**Context:** Open Decision O5 defines the architectural mandate for using a generated API client, resolving through O21 (Generated API Client / Types from governed OpenAPI contract). The concrete generator tool was deferred to implementation.

## Candidate Evaluation

| Generator | TypeScript Generation | React/Web Compat. | React Native Compat. | Reproducibility | CI Integration | D07 Shared Contract Support | Assessment |
|---|---|---|---|---|---|---|---|
| **OpenAPI Generator** (Java-based) | Yes | High | High | High (via Maven/NPM) | High | High (via central library) | Highly robust, but heavier dependency chain (requires JVM or Docker during frontend build). |
| **NSwag** | Yes | High | High | High | High | High | excellent C# support, somewhat more verbose in TS output compared to others. |
| **Orval** | Yes (excellent) | High (Native React Query support) | High (React Query) | High (Node-based) | High (NPM integration) | High (can generate shared pure functions + hooks) | Node-native, highly tuned for modern TS/React/RN stacks, supports both raw Axios/Fetch and query hooks independently. |

## Selection

**Selected Tool:** `Orval`

**Rationale:**
Orval was selected as an implementation-level choice because it runs natively in the Node ecosystem (aligning perfectly with the Frontend and Mobile toolchains without requiring Java or Docker to generate code), produces highly idiomatic TypeScript, and directly supports the architectural requirements of D07 (Shared Generated Contract). 

While it optionally supports React Query, its base generation creates pure fetch/axios functions and TypeScript models. This strictly fulfills the required criteria without introducing unapproved architectural mandates.

**Status:** Implementation-Level Choice (Does not alter Phase 4 Baseline or O21).
