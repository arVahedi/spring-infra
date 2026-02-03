# Spring-Infra
![CI](https://github.com/arVahedi/spring-infra/actions/workflows/ci.yaml/badge.svg)
[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/arVahedi/spring-infra)

Spring Boot 4.x infrastructure template for building domain-oriented services with a clean separation of HTTP, application, domain, and persistence layers.

For a high-level overview and evolving notes, see the DeepWiki page linked in the badge above.

## Architecture
This repo follows the Domain Service Architecture Pattern. Read the guide in `misc/document/domain_service_architecture_guide.md`.

## Requirements
- Java 21
- Maven (use `mvnw` if needed)
- Docker + Docker Compose (for dependency services)

## Quick start
First time (provision dependencies):

```bash
docker-compose up -d
```

Next times (restart dependencies):

```bash
docker-compose start
```

Run the app locally:

```bash
mvn spring-boot:run
```

Flyway will initialize the database on startup.

## Build & test

```bash
mvn clean package
```

```bash
mvn test
```

```bash
mvn verify
```

## Project layout
- `src/main/java/org/springinfra`: core infrastructure (security, web, data, utilities).
- `src/main/java/examples`: sample domain, services, mappers, and controllers.
- `src/main/resources`: configuration and resources (filtered during Maven build).
- `src/main/webapp`: JSP/static web assets (WAR packaging).
- `src/test/java`: tests and test utilities.

## Security & configuration
- Dependencies are managed via `docker-compose.yml`.
- Keycloak setup steps are in `keycloak.md` (ensure roles end with `_AUTHORITY`).
- Avoid committing secrets; use environment-specific configuration under `src/main/resources`.
