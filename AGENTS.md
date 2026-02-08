# Repository Guidelines

## Architectural Guide & Collaboration Convention
- Read and follow all development guide files at [./misc/document/](./misc/document) : 
  - Architecture guide: [domain_service_architecture_guide.md](./misc/document/domain_service_architecture_guide.md)
  - MapStruct integration guide: [mapstruct_integration_guide.md](./misc/document/mapstruct_integration_guide.md)

## Project Structure & Module Organization
- `src/main/java/org/springinfra`: core Spring Boot infrastructure (security, web, data, utilities).
- `src/main/java/examples`: sample domain, services, mappers, and controllers.
- `src/main/resources`: configuration and resource files; properties are filtered during the Maven build.
- `src/main/webapp`: JSP/static web assets (WAR packaging).
- `src/test/java`: test utilities, annotations, and tests (mirror package layout).
- `target`: build output (generated, do not edit).

## Build, Test, and Development Commands
- `mvn clean package`: compile, run tests, and build the WAR into `target/`.
- `mvn test`: run unit/integration tests via the Spring Boot test starter.
- `mvn spring-boot:run`: run the application locally (uses Java 21).
- `docker-compose up -d`: start required dependencies for first-time setup.
- `docker-compose start`: restart previously created containers.

## Coding Style & Naming Conventions
- Java 21, Spring Boot 4.x project; follow standard Java conventions (4-space indentation, braces on the same line).
- Package naming stays under `org.springinfra.*`.
- Use Lombok and MapStruct where applicable; keep annotations consistent with existing classes.
- Keep constants in `springinfra.assets` and follow existing naming (UPPER_SNAKE_CASE).
- All field access operations must be qualified (e.g., `this.field`) to avoid confusion with locals or parameters.

## Testing Guidelines
- Any code changes must be tested by at least one of unit/integration test(s) or even both.
- Tests live in `src/test/java` and should be named `*Test` (JUnit 6, Mockito 5). Integration tests use `*IT.java`.
- Use `@IntegrationTest` for full-context tests (it wires `@SpringBootTest`, `@AutoConfigureMockMvc`, test profile, and `MockIdentityProviderConfig`).
- The test profile uses H2 and in-memory identity provider settings (`src/test/resources/application-test.properties`).
- Database cleanup is handled by `CleanupDBTestExecutionListener` after each test; avoid manual truncation in tests unless needed.
- Use `@WithMockJwt` to declare authorities for secured endpoints; keep method names `when...then...`.
- Prefer Spring Boot testing utilities for integration tests; keep unit tests fast and isolated. 
- If adding new behavior, include tests that cover both happy-path and failure cases.

## Commit & Pull Request Guidelines
- Commit messages in this repo are short and past-tense (e.g., “Updated Dockerfile”, “Fixed issue in …”).
- Keep commits focused on a single change or concern.
- No formal PR template is present; include a concise summary, testing notes (commands run), and any config or migration steps.

## Code Review Guidelines
- Implementation details should be strictly aligned with the project architecture guide at ./misc/document/domain_service_architecture_guide.md
- Consider clean-architecture and clean-code principals, secure coding standards, and Java best practices
- Ensure all above guidelines are met
- Check existing tests to cover PR changes both happy-path and failure cases
- Require a brief risk/impact note (behavioral change, data migration, perf/security impact)
- Verify transaction boundaries and error handling in service layers
- Check MapStruct/Lombok usage doesn’t hide nullability or mapping gaps
- Ensure DTO validation is at controller edge, not in domain services
- Confirm repository queries are indexed/efficient and pagination is used where needed
- Look for logging of PII/secrets and ensure log levels are appropriate
- Require test evidence (command + key assertions) and note any gaps
- Validate API changes include backward-compat notes and OpenAPI docs if present

## Security & Configuration Notes
- Dependency services are managed via `docker-compose.yml`.
- Keycloak setup steps are documented in `keycloak.md`; ensure roles end with `_AUTHORITY` when following that guide.
- Avoid committing secrets; use environment-specific configuration in `src/main/resources`.
