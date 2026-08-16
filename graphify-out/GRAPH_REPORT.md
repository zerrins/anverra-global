# Graph Report - anverra-global  (2026-08-16)

## Corpus Check
- 436 files · ~344,362 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 1739 nodes · 3623 edges · 150 communities (97 shown, 53 thin omitted)
- Extraction: 84% EXTRACTED · 16% INFERRED · 0% AMBIGUOUS · INFERRED: 593 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- Commission Persistence & Adapter
- Frontend Policy API Client
- Mobile API Generated Clients
- Frontend API Fakers & MSW
- Spring Security & JWT Filter
- Organization Persistence Layer
- AI Rules & Agent Configuration
- Policy API Tests
- OrganizationScope Resolution
- Reporting Read Model
- Policy REST Controller
- Policy Persistence Entity
- Cross-Module Event Flow
- Commission & Policy Application Services
- Domain Events Registry
- Mobile Generated Models
- Policy Persistence Adapter
- Product Domain & Persistence
- Frontend Build Configuration
- Reporting Integration Tests
- Test Infrastructure
- Module Cluster 21
- Frontend Components
- Module Cluster 23
- Module Cluster 24
- Module Cluster 25
- Mobile Components
- Module Cluster 27
- Module Cluster 28
- Frontend Components
- Frontend Components
- Module Cluster 31
- Module Cluster 32
- Frontend Components
- Frontend Components
- Module Cluster 35
- Test Infrastructure
- Frontend Components
- Module Cluster 38
- Module Cluster 39
- Identity Module Internals
- Module Cluster 41
- Test Infrastructure
- Frontend Components
- Mobile Components
- Module Cluster 45
- Test Infrastructure
- Frontend Components
- Frontend Components
- Frontend Components
- Module Cluster 50
- Test Infrastructure
- Module Cluster 52
- Module Cluster 53
- Mobile Components
- Mobile Components
- Mobile Components
- Module Cluster 57
- Module Cluster 58
- Test Infrastructure
- Frontend Components
- Mobile Components
- Module Cluster 62
- Module Cluster 63
- Module Cluster 64
- Test Infrastructure
- Frontend Components
- Module Cluster 67
- Frontend Components
- Mobile Components
- Module Cluster 70
- Frontend Components
- Module Cluster 72
- Frontend Components
- Mobile Components
- Mobile Components
- Test Infrastructure
- Test Infrastructure
- Frontend Components
- Frontend Components
- Mobile Components
- Mobile Components
- Mobile Components
- Mobile Components
- Mobile Components
- Mobile Components
- Mobile Components
- Mobile Components
- Test Infrastructure
- Module Cluster 89
- Module Cluster 90
- Mobile Components
- Frontend Components
- Frontend Components
- Frontend Components
- Frontend Components
- Frontend Components
- Frontend Components
- Frontend Components
- Mobile Components
- Mobile Components
- Mobile Components
- Module Cluster 102
- Module Cluster 103
- Module Cluster 104
- Module Cluster 111
- Module Cluster 112
- Module Cluster 113
- Module Cluster 114
- Module Cluster 115
- Module Cluster 116
- Module Cluster 117
- Module Cluster 118
- Module Cluster 119
- Module Cluster 120
- Module Cluster 121
- Module Cluster 122
- Module Cluster 123
- Module Cluster 124
- Module Cluster 125
- Module Cluster 126
- Module Cluster 127
- Test Infrastructure
- Module Cluster 129
- Module Cluster 130
- Module Cluster 131
- Module Cluster 132
- Module Cluster 133
- Frontend Components
- Frontend Components
- Frontend Components
- Frontend Components
- Mobile Components
- Mobile Components
- Mobile Components
- Module Cluster 149

## God Nodes (most connected - your core abstractions)
1. `OrganizationScope` - 52 edges
2. `PolicyEntity` - 44 edges
3. `Policy` - 43 edges
4. `PolicyApiTest` - 35 edges
5. `Product` - 33 edges
6. `ReportingReadModelEntity` - 32 edges
7. `CommissionEntity` - 31 edges
8. `PolicyManagementApplicationService` - 27 edges
9. `ReportingPersistenceTest` - 27 edges
10. `OrganizationPersistenceTest` - 26 edges

## Surprising Connections (you probably didn't know these)
- `Mobile UX Rule` --references--> `REQ-DEC-009: Mobile UX Requirements`  [INFERRED]
  .ai/rules/mobile-ux.md → docs/06-requirements/decisions/REQ-DEC-009-mobile-ux.md
- `Mobile Agents Config` --references--> `REQ-DEC-009: Mobile UX Requirements`  [INFERRED]
  mobile/AGENTS.md → docs/06-requirements/decisions/REQ-DEC-009-mobile-ux.md
- `Project Agents Config` --references--> `Graphify Agent Rule`  [INFERRED]
  AGENTS.md → .agents/rules/graphify.md
- `Project Agents Config` --references--> `AI Constitution Rule`  [INFERRED]
  AGENTS.md → .ai/rules/constitution.md
- `AI Constitution Rule` --references--> `Engineering Constitution`  [EXTRACTED]
  .ai/rules/constitution.md → docs/01-constitution/README.md

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Core Business Modules** — docs_02_repository_blueprint_02_business_modules_01_identity_blueprint_identity_module, docs_02_repository_blueprint_02_business_modules_02_customer_blueprint_customer_module, docs_02_repository_blueprint_02_business_modules_03_product_blueprint_product_module, docs_02_repository_blueprint_02_business_modules_04_policy_blueprint_policy_module, docs_02_repository_blueprint_02_business_modules_05_commission_blueprint_commission_module, docs_02_repository_blueprint_02_business_modules_07_reporting_blueprint_reporting_module [EXTRACTED 1.00]
- **Phase 1-5 Authority Hierarchy** — docs_01_constitution_readme_constitution, docs_02_repository_blueprint_01_system_repository_blueprint_01_anverra_system_blueprint_system_blueprint, docs_03_technology_01_backend_blueprint_backend_technology, docs_04_system_design_01_backend_implementation_architecture_backend_impl_arch, docs_05_implementation_00_phase_5_overview_phase5_overview [EXTRACTED 1.00]
- **Authentication & Authorization Chain** — auth0_oidc, jwt_rbac_authorization, organization_scope, docs_06_requirements_decisions_req_dec_010_jwt_contract_jwt_contract, docs_04_system_design_06_security_implementation_architecture_security_impl_arch [EXTRACTED 0.95]

## Communities (150 total, 53 thin omitted)

### Community 0 - "Commission Persistence & Adapter"
Cohesion: 0.07
Nodes (32): CommissionPersistenceAdapter, Component, Override, CommissionManagementServiceImpl, ApplicationEventPublisher, Override, Service, Transactional (+24 more)

### Community 1 - "Frontend Policy API Client"
Cohesion: 0.03
Nodes (68): ActivatePolicyMutationBody, ActivatePolicyMutationError, ActivatePolicyMutationResult, activatePolicyResponse, activatePolicyResponse200, activatePolicyResponseSuccess, ConfigureCommissionMutationBody, ConfigureCommissionMutationError (+60 more)

### Community 2 - "Mobile API Generated Clients"
Cohesion: 0.03
Nodes (68): ActivatePolicyMutationBody, ActivatePolicyMutationError, ActivatePolicyMutationResult, activatePolicyResponse, activatePolicyResponse200, activatePolicyResponseSuccess, ConfigureCommissionMutationBody, ConfigureCommissionMutationError (+60 more)

### Community 3 - "Frontend API Fakers & MSW"
Cohesion: 0.07
Nodes (36): getCreatePolicyResponseMock(), getGetPolicyResponseMock(), getListPoliciesResponseMock(), getResolvePolicyResponseMock(), getUpdatePolicyResponseMock(), getActivatePolicyMockHandler(), getConfigureCommissionMockHandler(), getCreatePolicyMockHandler() (+28 more)

### Community 4 - "Spring Security & JWT Filter"
Cohesion: 0.08
Nodes (29): AbstractAuthenticationToken, AuthenticationEntryPoint, AudienceValidator, Jwt, Override, CustomJwtAuthenticationConverter, Jwt, Override (+21 more)

### Community 5 - "Organization Persistence Layer"
Cohesion: 0.06
Nodes (12): BranchRecord, Table, BranchRepository, Table, OrganizationMembershipRecord, OrganizationMembershipRepository, Component, OrganizationMembershipDto (+4 more)

### Community 6 - "AI Rules & Agent Configuration"
Cohesion: 0.06
Nodes (46): Project Agents Config, Graphify Agent Rule, AI Constitution Rule, Mobile UX Rule, Web UX Rule, ArchUnit Architecture Verification, Auth0 OIDC Provider, Company Vision (+38 more)

### Community 7 - "Policy API Tests"
Cohesion: 0.17
Nodes (12): AutoConfigureMockMvc, BeforeEach, DynamicPropertyRegistry, DynamicPropertySource, JdbcTemplate, MockMvc, PostgreSQLContainer, SpringBootTest (+4 more)

### Community 8 - "OrganizationScope Resolution"
Cohesion: 0.15
Nodes (12): Override, AutoConfigureTestDatabase, BeforeEach, DataJdbcTest, DynamicPropertyRegistry, DynamicPropertySource, Import, JdbcTemplate (+4 more)

### Community 10 - "Policy REST Controller"
Cohesion: 0.18
Nodes (18): ConfigureCommissionRequest, CreatePolicyRequest, GetMapping, Page, Pageable, PostMapping, Principal, PutMapping (+10 more)

### Community 11 - "Policy Persistence Entity"
Cohesion: 0.19
Nodes (4): Table, PolicyEntity, Test, Test

### Community 12 - "Cross-Module Event Flow"
Cohesion: 0.15
Nodes (11): AutoConfigureTestDatabase, BeforeEach, DataJdbcTest, DynamicPropertyRegistry, DynamicPropertySource, Import, JdbcTemplate, PostgreSQLContainer (+3 more)

### Community 13 - "Commission & Policy Application Services"
Cohesion: 0.11
Nodes (11): CommissionManagementService, ApplicationEventPublisher, Page, Pageable, Service, PolicyManagementApplicationService, Page, Pageable (+3 more)

### Community 14 - "Domain Events Registry"
Cohesion: 0.14
Nodes (11): CommissionConfiguredEvent, PolicyActivatedEvent, PolicyCreatedEvent, PolicyPremiumUpdatedEvent, PolicyReactivatedEvent, ReportingOutboundPort, Service, ReportingApplicationService (+3 more)

### Community 15 - "Mobile Generated Models"
Cohesion: 0.09
Nodes (15): CommissionStatisticsResponse, ConfigureCommissionRequest, CreatePolicyRequest, LifecycleRequest, ListPoliciesParams, Pageable, PageableObject, PagePolicyResponse (+7 more)

### Community 16 - "Policy Persistence Adapter"
Cohesion: 0.14
Nodes (9): Component, Override, Page, Pageable, PolicyPersistenceAdapter, Page, Pageable, PolicyRepository (+1 more)

### Community 17 - "Product Domain & Persistence"
Cohesion: 0.18
Nodes (6): Product, ProductStatus, ACTIVE, INACTIVE, Test, ProductTest

### Community 18 - "Frontend Build Configuration"
Cohesion: 0.08
Nodes (25): compilerOptions, allowArbitraryExtensions, allowImportingTsExtensions, erasableSyntaxOnly, isolatedModules, jsx, lib, module (+17 more)

### Community 19 - "Reporting Integration Tests"
Cohesion: 0.18
Nodes (12): ApplicationEventPublisher, AutoConfigureTestDatabase, BeforeEach, DynamicPropertyRegistry, DynamicPropertySource, JdbcTemplate, PostgreSQLContainer, SpringBootTest (+4 more)

### Community 20 - "Test Infrastructure"
Cohesion: 0.18
Nodes (12): AutoConfigureMockMvc, BeforeEach, DynamicPropertyRegistry, DynamicPropertySource, JdbcTemplate, MockMvc, PostgreSQLContainer, SpringBootTest (+4 more)

### Community 22 - "Frontend Components"
Cohesion: 0.12
Nodes (21): getCommissionStatistics(), GetCommissionStatisticsQueryError, GetCommissionStatisticsQueryResult, getCommissionStatisticsResponse, getCommissionStatisticsResponse200, getCommissionStatisticsResponseSuccess, getGetCommissionStatisticsQueryKey(), getGetCommissionStatisticsQueryOptions() (+13 more)

### Community 23 - "Module Cluster 23"
Cohesion: 0.18
Nodes (7): AccessDeniedException, OrganizationMembershipDto, OrganizationPersistencePort, Service, OrganizationScopeResolutionServiceImpl, OrganizationScope, OrganizationScopeResolutionService

### Community 24 - "Module Cluster 24"
Cohesion: 0.28
Nodes (3): CommissionEntity, Table, Test

### Community 25 - "Module Cluster 25"
Cohesion: 0.21
Nodes (9): Page, Pageable, ProductRepositoryPort, Page, Pageable, Service, Transactional, ProductManagementApplicationService (+1 more)

### Community 26 - "Mobile Components"
Cohesion: 0.09
Nodes (23): expo, expo-status-bar, dependencies, expo, expo-status-bar, react, react-native, react-native-auth0 (+15 more)

### Community 27 - "Module Cluster 27"
Cohesion: 0.20
Nodes (8): GetMapping, Principal, RequestMapping, ResponseEntity, RestController, ReportingController, CommissionStatisticsResponse, PolicyStatisticsResponse

### Community 28 - "Module Cluster 28"
Cohesion: 0.10
Nodes (21): eslint, eslint-config-expo, jest-expo, devDependencies, eslint, eslint-config-expo, jest-expo, orval (+13 more)

### Community 29 - "Frontend Components"
Cohesion: 0.10
Nodes (19): compilerOptions, allowImportingTsExtensions, erasableSyntaxOnly, lib, module, moduleDetection, noEmit, noFallthroughCasesInSwitch (+11 more)

### Community 30 - "Frontend Components"
Cohesion: 0.11
Nodes (19): @auth0/auth0-react, dependencies, @auth0/auth0-react, @hookform/resolvers, lucide-react, react, react-dom, react-hook-form (+11 more)

### Community 31 - "Module Cluster 31"
Cohesion: 0.20
Nodes (11): GetMapping, Page, Pageable, PostMapping, PutMapping, RequestMapping, ResponseEntity, RestController (+3 more)

### Community 33 - "Frontend Components"
Cohesion: 0.11
Nodes (19): @faker-js/faker, devDependencies, @faker-js/faker, jsdom, oxlint, @testing-library/react, @types/react, @types/react-dom (+11 more)

### Community 34 - "Frontend Components"
Cohesion: 0.17
Nodes (13): createPolicy(), getCreatePolicyMutationOptions(), getCreatePolicyUrl(), useCreatePolicy(), useListPolicies(), withQueryKey(), CreatePolicyModal(), FormData (+5 more)

### Community 35 - "Module Cluster 35"
Cohesion: 0.24
Nodes (7): CommissionStatisticsResponse, Component, NamedParameterJdbcTemplate, Override, PolicyStatisticsResponse, ReportingPersistenceAdapter, MapSqlParameterSource

### Community 36 - "Test Infrastructure"
Cohesion: 0.21
Nodes (12): AutoConfigureMockMvc, BeforeEach, DynamicPropertyRegistry, DynamicPropertySource, JdbcTemplate, MockMvc, PostgreSQLContainer, SpringBootTest (+4 more)

### Community 37 - "Frontend Components"
Cohesion: 0.15
Nodes (10): AccessDenied(), AppShell(), Header(), ProtectedRoute(), Sidebar(), Dashboard, PolicyDetails, PolicyList (+2 more)

### Community 38 - "Module Cluster 38"
Cohesion: 0.25
Nodes (16): cmd_logs(), cmd_logs_ui(), cmd_restart(), cmd_start(), cmd_status(), cmd_stop(), error(), _is_running() (+8 more)

### Community 39 - "Module Cluster 39"
Cohesion: 0.21
Nodes (8): Component, NamedParameterJdbcTemplate, Override, Page, Pageable, ProductPersistenceAdapter, ProductRepository, PagingAndSortingRepository

### Community 40 - "Identity Module Internals"
Cohesion: 0.21
Nodes (10): AutoConfigureMockMvc, DynamicPropertyRegistry, DynamicPropertySource, JwtDecoder, MockMvc, PostgreSQLContainer, SpringBootTest, Test (+2 more)

### Community 41 - "Module Cluster 41"
Cohesion: 0.28
Nodes (7): GlobalExceptionHandler, AuthenticationException, ControllerAdvice, ExceptionHandler, HttpRequestMethodNotSupportedException, OptimisticLockingFailureException, ProblemDetail

### Community 42 - "Test Infrastructure"
Cohesion: 0.19
Nodes (12): AutoConfigureMockMvc, DynamicPropertyRegistry, DynamicPropertySource, MockMvc, PostgreSQLContainer, SpringBootTest, Test, Testcontainers (+4 more)

### Community 43 - "Frontend Components"
Cohesion: 0.17
Nodes (15): activatePolicy(), getActivatePolicyMutationOptions(), getActivatePolicyUrl(), getReactivatePolicyMutationOptions(), getReactivatePolicyUrl(), getUpdatePremiumMutationOptions(), getUpdatePremiumUrl(), reactivatePolicy() (+7 more)

### Community 44 - "Mobile Components"
Cohesion: 0.18
Nodes (11): useAuth(), MainTabsParamList, PoliciesStack, RootNavigator(), RootStackParamList, Stack, Tab, LoginScreen() (+3 more)

### Community 45 - "Module Cluster 45"
Cohesion: 0.23
Nodes (11): CommissionRepository, ApplicationEvents, AutoConfigureTestDatabase, DynamicPropertyRegistry, DynamicPropertySource, JdbcTemplate, PostgreSQLContainer, RecordApplicationEvents (+3 more)

### Community 47 - "Frontend Components"
Cohesion: 0.17
Nodes (12): plugins, useGetPolicy(), PoliciesStackParamList, NavigationProp, PoliciesScreen(), styles, PolicyDetailsRouteProp, PolicyDetailsScreen() (+4 more)

### Community 48 - "Frontend Components"
Cohesion: 0.17
Nodes (7): App(), queryClient, ErrorBoundary, Props, State, shouldRetry(), AppRouter()

### Community 49 - "Frontend Components"
Cohesion: 0.26
Nodes (8): useGetCommissionStatistics(), useGetPolicyStatistics(), withQueryKey(), ApiErrorAlert(), ApiErrorProps, Dashboard(), queryClient, ReportingDashboard()

### Community 50 - "Module Cluster 50"
Cohesion: 0.21
Nodes (4): CloudflareR2Adapter, Component, Override, DocumentStoragePort

### Community 51 - "Test Infrastructure"
Cohesion: 0.29
Nodes (7): CreateProductRequest, MockMvc, Test, WithMockUser, ProductControllerTest, ObjectMapper, WebMvcTest

### Community 52 - "Module Cluster 52"
Cohesion: 0.15
Nodes (11): ProductCategory, COMMERCIAL_INSURANCE, ENGINEERING_INSURANCE, FIRE_INSURANCE, HEALTH_INSURANCE, LIABILITY_INSURANCE, LIFE_INSURANCE, MARINE_INSURANCE (+3 more)

### Community 53 - "Module Cluster 53"
Cohesion: 0.31
Nodes (8): AsyncConfigurer, ApplicationConfiguration, Bean, Configuration, Override, EnableAsync, EnableScheduling, ThreadPoolTaskExecutor

### Community 54 - "Mobile Components"
Cohesion: 0.25
Nodes (7): App(), queryClient, AuthContext, AuthContextType, AuthProvider(), setAccessTokenProvider(), TestComponent()

### Community 55 - "Mobile Components"
Cohesion: 0.29
Nodes (8): activatePolicy(), getActivatePolicyMutationOptions(), getActivatePolicyUrl(), useActivatePolicy(), customInstance(), ErrorType, getAccessToken(), getAccessTokenFn()

### Community 56 - "Mobile Components"
Cohesion: 0.18
Nodes (11): getCommissionStatistics(), getGetCommissionStatisticsQueryKey(), getGetCommissionStatisticsQueryOptions(), getGetCommissionStatisticsUrl(), getGetPolicyStatisticsQueryKey(), getGetPolicyStatisticsQueryOptions(), getGetPolicyStatisticsUrl(), getPolicyStatistics() (+3 more)

### Community 57 - "Module Cluster 57"
Cohesion: 0.33
Nodes (6): mvnw script, clean(), die(), exec_maven(), set_java_home(), verbose()

### Community 59 - "Test Infrastructure"
Cohesion: 0.36
Nodes (8): AutoConfigureTestDatabase, DataJdbcTest, DynamicPropertyRegistry, DynamicPropertySource, JdbcTemplate, PostgreSQLContainer, Testcontainers, PolicyPersistenceTest

### Community 60 - "Frontend Components"
Cohesion: 0.29
Nodes (7): AuthProvider(), AuthProviderProps, AuthTokenInjector(), resetTokenState(), setTokenGetter(), handlers, server

### Community 61 - "Mobile Components"
Cohesion: 0.20
Nodes (10): scripts, android, build, generate:api, ios, lint, start, test (+2 more)

### Community 62 - "Module Cluster 62"
Cohesion: 0.20
Nodes (8): fs, http, LOG_FILES, path, PORT, ROOT, server, { URL }

### Community 63 - "Module Cluster 63"
Cohesion: 0.36
Nodes (5): EventRetryScheduler, Component, IncompleteEventPublications, Logger, Scheduled

### Community 64 - "Module Cluster 64"
Cohesion: 0.31
Nodes (4): PolicyDeactivatedEvent, ApplicationModuleListener, Component, TestExecutorListener

### Community 65 - "Test Infrastructure"
Cohesion: 0.39
Nodes (7): CommissionPersistenceTest, AutoConfigureTestDatabase, DataJdbcTest, DynamicPropertyRegistry, DynamicPropertySource, PostgreSQLContainer, Testcontainers

### Community 66 - "Frontend Components"
Cohesion: 0.25
Nodes (8): configureCommission(), getConfigureCommissionMutationOptions(), getConfigureCommissionUrl(), useConfigureCommission(), ConfigureCommissionModal(), FormData, Props, schema

### Community 67 - "Module Cluster 67"
Cohesion: 0.36
Nodes (4): AnverraGlobalApplication, Test, ModulithVerificationTests, SpringBootApplication

### Community 68 - "Frontend Components"
Cohesion: 0.29
Nodes (7): scripts, build, dev, lint, preview, test, test:watch

### Community 69 - "Mobile Components"
Cohesion: 0.29
Nodes (6): compilerOptions, strict, exclude, extends, expo/tsconfig.base, **/__tests__/*

### Community 70 - "Module Cluster 70"
Cohesion: 0.33
Nodes (4): PolicyStatus, ACTIVE, DRAFT, INACTIVE

### Community 71 - "Frontend Components"
Cohesion: 0.33
Nodes (5): rules, react/only-export-components, react/rules-of-hooks, $schema, warn

### Community 72 - "Module Cluster 72"
Cohesion: 0.40
Nodes (5): API Technology Blueprint, API Transport Implementation Architecture, OpenAPI Generator Evaluation, REQ-DEC-007: API Requirements, OpenAPI 3.0 Contract

### Community 73 - "Frontend Components"
Cohesion: 0.40
Nodes (4): name, private, type, version

### Community 74 - "Mobile Components"
Cohesion: 0.40
Nodes (4): main, name, private, version

### Community 75 - "Mobile Components"
Cohesion: 0.40
Nodes (5): getListPoliciesQueryKey(), getListPoliciesQueryOptions(), getListPoliciesUrl(), listPolicies(), useListPolicies()

### Community 76 - "Test Infrastructure"
Cohesion: 0.83
Nodes (3): AnalyzeClasses, ArchRule, ArchitectureVerificationTests

### Community 78 - "Frontend Components"
Cohesion: 0.50
Nodes (4): getGetPolicyQueryKey(), getGetPolicyQueryOptions(), getGetPolicyUrl(), getPolicy()

### Community 79 - "Frontend Components"
Cohesion: 0.50
Nodes (4): getListPoliciesQueryKey(), getListPoliciesQueryOptions(), getListPoliciesUrl(), listPolicies()

### Community 80 - "Mobile Components"
Cohesion: 0.50
Nodes (4): configureCommission(), getConfigureCommissionMutationOptions(), getConfigureCommissionUrl(), useConfigureCommission()

### Community 81 - "Mobile Components"
Cohesion: 0.50
Nodes (4): createPolicy(), getCreatePolicyMutationOptions(), getCreatePolicyUrl(), useCreatePolicy()

### Community 82 - "Mobile Components"
Cohesion: 0.50
Nodes (4): deactivatePolicy(), getDeactivatePolicyMutationOptions(), getDeactivatePolicyUrl(), useDeactivatePolicy()

### Community 83 - "Mobile Components"
Cohesion: 0.50
Nodes (4): getGetPolicyQueryKey(), getGetPolicyQueryOptions(), getGetPolicyUrl(), getPolicy()

### Community 84 - "Mobile Components"
Cohesion: 0.50
Nodes (4): getReactivatePolicyMutationOptions(), getReactivatePolicyUrl(), reactivatePolicy(), useReactivatePolicy()

### Community 85 - "Mobile Components"
Cohesion: 0.50
Nodes (4): getResolvePolicyMutationOptions(), getResolvePolicyUrl(), resolvePolicy(), useResolvePolicy()

### Community 86 - "Mobile Components"
Cohesion: 0.50
Nodes (4): getUpdatePolicyMutationOptions(), getUpdatePolicyUrl(), updatePolicy(), useUpdatePolicy()

### Community 87 - "Mobile Components"
Cohesion: 0.50
Nodes (4): getUpdatePremiumMutationOptions(), getUpdatePremiumUrl(), updatePremium(), useUpdatePremium()

### Community 89 - "Module Cluster 89"
Cohesion: 0.67
Nodes (3): Persistence Technology Blueprint, Persistence Implementation Architecture, PostgreSQL Persistence

## Knowledge Gaps
- **367 isolated node(s):** `com.anverraglobal:backend`, `UNSET`, `CONFIGURED`, `FIXED`, `PERCENTAGE` (+362 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **53 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `react` connect `Frontend Components` to `Frontend Components`, `Frontend Components`, `Frontend Components`, `Mobile Components`, `Frontend Components`, `Mobile Components`, `Frontend Components`?**
  _High betweenness centrality (0.041) - this node is a cross-community bridge._
- **Why does `OrganizationScope` connect `Module Cluster 23` to `Module Cluster 35`, `OrganizationScope Resolution`, `Cross-Module Event Flow`, `Commission & Policy Application Services`, `Domain Events Registry`, `Policy Persistence Adapter`, `Test Infrastructure`, `Module Cluster 21`, `Module Cluster 58`, `Module Cluster 27`?**
  _High betweenness centrality (0.040) - this node is a cross-community bridge._
- **Why does `CommissionRepository` connect `Module Cluster 45` to `Commission Persistence & Adapter`, `Test Infrastructure`, `Organization Persistence Layer`, `Policy API Tests`, `Module Cluster 24`?**
  _High betweenness centrality (0.023) - this node is a cross-community bridge._
- **Are the 8 inferred relationships involving `OrganizationScope` (e.g. with `.setUp()` and `.testDataEntryForbiddenOnCommissionStatistics()`) actually correct?**
  _`OrganizationScope` has 8 INFERRED edges - model-reasoned connections that need verification._
- **Are the 4 inferred relationships involving `PolicyEntity` (e.g. with `.testOptimisticLocking()` and `.testSaveAndLoadPolicy()`) actually correct?**
  _`PolicyEntity` has 4 INFERRED edges - model-reasoned connections that need verification._
- **What connects `com.anverraglobal:backend`, `UNSET`, `CONFIGURED` to the rest of the system?**
  _367 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Commission Persistence & Adapter` be split into smaller, more focused modules?**
  _Cohesion score 0.07263157894736842 - nodes in this community are weakly interconnected._