# Repository Guidelines

## Project Structure & Module Organization
- `src/main/java/springinfra`: core Spring Boot application code (aspects, services, utilities, security, database helpers).
- `src/main/resources`: configuration and resource files; properties are filtered during the Maven build.
- `src/main/webapp`: JSP/static web assets (WAR packaging).
- `src/test/java`: test utilities and test classes, organized in mirror packages.
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

## Testing Guidelines
- Tests live in `src/test/java` and should be named `*Test` (JUnit 6, Mockito 5).
- Unit tests files should follow naming convention like `*Test.java` and integration test should follow `*IT.java`.
- Unit / Integration tests should share the same mirror package; for example, controllers in `examples.rest` use tests in `examples.rest`.
- Prefer Spring Boot testing utilities for integration tests; keep unit tests fast and isolated.
- If adding new behavior, include tests that cover both happy-path and failure cases.

## Commit & Pull Request Guidelines
- Commit messages in this repo are short and past-tense (e.g., “Updated Dockerfile”, “Fixed issue in …”).
- Keep commits focused on a single change or concern.
- No formal PR template is present; include a concise summary, testing notes (commands run), and any config or migration steps.

## Security & Configuration Notes
- Dependency services are managed via `docker-compose.yml`.
- Keycloak setup steps are documented in `keycloak.md`; ensure roles end with `_AUTHORITY` when following that guide.
- Avoid committing secrets; use environment-specific configuration in `src/main/resources`.
