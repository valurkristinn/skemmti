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
| --- | --- | --- |
| ADMIN | `admin` | 1 |
| ORGANIZER | `Oktoberfest` | 2 |
| MEMBER | `Jon Jonsson` | 3 |

Users and events created at runtime get ids from 100 upwards.

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
```
