# Development

Dev setup and conventions for Skemmti. For the API, see [API.md](API.md).

## Requirements

- Java 21
- Maven (use the bundled wrapper `./mvnw`)

## Getting started

```bash
cp .env.example .env
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080`.

`.env` is read by `spring.config.import` in `application.properties` and must contain:

| variable | description |
| --- | --- |
| `JWT_SECRET` | HMAC key used to sign and verify JWTs. Min. 32 characters. |

## Database

H2 in-memory, recreated on every start and seeded from `src/main/resources/data.sql`.
Nothing persists between runs.

Seeded users (all with password `passw`):

| role | username | userId |
| --- | ---| ---|
| admin | "admin" | 1 |
| organizer | "oktoberfest"| 2 |
|member | "jon.jonsson"| 3|

Users and events created at runtime get ids from 100 upwards.

New users must follow these rules (the seed password `passw` predates them):

- Username: 3–32 characters, only lowercase letters, numbers, `.`, `_` and `-` (e.g. `jon.jonsson`), capitals are converted to lowercase
- Password: 8–32 characters, at least one number and one capital letter (A–Z), no spaces (e.g. `Password123`)

## Project layout

```
src/main/java/is/hi/skemmti/
├── controller/   Endpoints, input validation
├── service/      Interfaces
│   └── impl/     Business logic
├── repository/   Spring Data JPA repositories
├── model/        JPA entities
├── security/     Security config
├── utils/        Utility classes 
└── exception/    Custom exceptions
```

## Conventions

**Validation** happens in the controller, via `Validation`, before anything is
passed to a service. Services only do business rules.

**Errors** are thrown as `ApiException` subclasses, `BadCredentialsException` is also thrown, it returns an 401 Unauthorized error with no message

| exception | status |
| --- | --- |
| `ApiException.InvalidData` | 400 |
| `ApiException.Forbidden` | 403 |
| `ApiException.NotFound` | 404 |
| `ApiException.Conflict` | 409 |

**Auth** uses JWT Tokens. `POST /login` and the signup endpoints return a raw
token string; send it as `Authorization: Bearer <token>`. The token carries
`uid` (user id) and `role`, and lives for 24 hours.

Path-level rules live in `SecurityConfig`.
Role checks beyond that (e.g. "only organizers may publish") live in the service implementations (Not final!).

**Images** are stored as base64 data URIs on the entity, max 2 MB, PNG or JPEG
only. `ImageUtil.validate` enforces this.

## Tests

```bash
./mvnw test

./mvnw test -Dtest=UserServiceTests # for running a single test class
```

Tests use the seeded data and are rolled back after each test.

All test classes extend `ContextBase` so they share one Spring context. Don't add
`@SpringBootTest` to a test class directly.

`ContextBase` holds shared constants and helpers: the seeded usernames `ADMIN`,
`ORGANIZER`, `MEMBER`, and `idOf(username)` to get a seeded user's id (don't hardcode ids).
Constants used by a single test class go at the top of that class.

Service tests extend `BaseServiceTests` (extends `ContextBase`), which provides
`adminId`, `organizerId` and `memberId`.

Endpoint tests extend `BaseControllerTests` (extends `ContextBase`), which provides
`mvc` and `getAuthHeader(username)`.

`getAuthHeader(username)` logs in as a seeded user and returns the auth header.

f.x
```java
mvc.perform(get("/requests/org").headers(getAuthHeader(ADMIN)))
```

- One case per test, named after what it checks
- Per endpoint: a happy path and at least one of 400/401/403/404
- Check the status and at most one or two response fields
- Tests must not depend on each other or on run order

## Deployment

Docker is used for deployments and runs with the `prod` profile which uses a persistent PostgreSQL db defined in `DB_URL`
