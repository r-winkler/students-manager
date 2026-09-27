---
description: Backend architectural and engineering standards
paths:
  - "**/src/main/**/*.java"
---

# Backend Engineering Standards

## 1. Architecture

The backend follows **hexagonal architecture (ports & adapters)**. Dependencies always point inward:

`adapter` -> `application` -> `domain`

- The `domain` layer must never depend on `application` or `adapter`.
- The `application` layer must never depend on `adapter`.
- Adapters depend only on application ports (interfaces), never on other adapters directly.

## 2. Package Structure

All backend source code must reside under the root package defined in `CLAUDE.md` using the following structure.

- `domain` -> Core business model and rules. No framework annotations, no Spring, no JPA.
  - `domain.model` -> Domain entities and value objects (plain Java, immutable where possible).
  - `domain.exception` -> Domain-level exceptions expressing business rule violations.
- `application` -> Use cases orchestrating domain logic. No web or persistence framework types.
  - `application.port.in` -> Input ports: one interface per use case (e.g. `CreateStudentUseCase`).
  - `application.port.out` -> Output ports: interfaces the application needs from the outside world (e.g. `StudentRepositoryPort`).
  - `application.service` -> Use case implementations. Implement input ports, depend only on output ports.
  - `application.exception` -> Application-level exceptions (e.g. not-found, conflict).
- `adapter.in.web` -> Driving/primary adapter. REST controllers, request/response DTOs, `@RestControllerAdvice`.
- `adapter.out.persistence` -> Driven/secondary adapter. JPA entities, Spring Data repositories, and classes implementing `application.port.out`.
- `config` -> Spring wiring and third-party configuration classes.
- `util` -> Stateless helper and utility classes.

## 3. Domain Layer

- Domain models must be plain Java classes/records, free of JPA (`@Entity`), Spring, or Jakarta Validation annotations.
- Business invariants and rules are enforced inside the domain model or domain services, not in controllers or persistence adapters.
- Domain exceptions describe business rule violations, independent of HTTP or persistence concerns.

## 4. Application Layer (Ports & Use Cases)

- Every use case is defined as an input port interface in `application.port.in` and implemented by exactly one class in `application.service`.
- Use case implementations depend only on output ports (`application.port.out`), never on adapter classes or JPA/Spring Data types directly.
- Output ports are defined by the application layer, in the vocabulary of the domain (e.g. `findById`, `save`), not the persistence technology.
- Application services define transactional boundaries (`@Transactional`) and return domain objects, not DTOs or entities.

## 5. Adapters

- **Web adapter** (`adapter.in.web`): controllers depend only on input ports (use cases). They map DTOs to domain objects (and back) and contain no business logic.
- **Persistence adapter** (`adapter.out.persistence`): implements output ports. Contains JPA entities and Spring Data JPA repositories, and maps between JPA entities and domain models.
- JPA entities and API DTOs must never be used outside their own adapter package.
- An adapter must not call another adapter directly; cross-adapter communication happens only through the application layer.

## 6. DTOs & Mapping

- API request/response DTOs live in `adapter.in.web` and must be implemented as immutable Java `record` types.
- JPA entities live in `adapter.out.persistence` and are strictly persistence models; they must never be exposed via API responses.
- Mapping between DTO <-> domain (web adapter) and domain <-> entity (persistence adapter) is done in dedicated mapper classes within the respective adapter package.

## 7. Dependency Injection

- Use constructor injection exclusively.
- Field injection (`@Autowired` on fields) is forbidden.
- Dependencies must be declared as `private final` fields.

## 8. API Design & Validation

- APIs must follow REST conventions using correct HTTP methods (`GET`, `POST`, `PUT`, `DELETE`).
- APIs must return appropriate HTTP status codes (`200`, `201`, `204`, etc.).
- Request validation must be performed using Jakarta Validation annotations on web-adapter DTOs.
- Validation must be applied using `@Valid` in controller method signatures.

## 9. Exception Handling & Error Responses

- Domain and application exceptions must not leak framework-specific details.
- All exceptions must be translated to HTTP responses centrally via `@RestControllerAdvice` in `adapter.in.web`.
- API error responses must follow RFC 7807 `ProblemDetail` format.
- Stack traces or internal system details must never be exposed to clients.

## 10. Configuration Management

- All infrastructure and framework configuration must reside in the `config` package.
- Prefer `@ConfigurationProperties` for type-safe configuration binding.
- Avoid scattered `@Value` annotations across the codebase.
