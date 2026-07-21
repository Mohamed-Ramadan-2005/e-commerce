# Design Notes

Brief record of the assumptions behind this codebase, the deliberate design choices. Written for whoever picks this project up next.

## Assumptions

- **Two fixed roles.** The domain only needs `USER` and `ADMIN`; there's no self-service
  role management beyond a single "promote to admin" action, and a promoted user loses `USER`
  in the process.
- **Single currency, no tax/shipping model.** `price` and `totalAmount` are plain numbers with no
  currency field, tax calculation, or shipping cost — out of scope for this API.
- **Synchronous, single-instance deployment.** No message queue, no distributed lock, no cache —
  reasonable for a small catalog/order service, but see the concurrency note below if this ever
  runs behind more than one instance or under real concurrent load.
- **MariaDB is the only supported datastore.** The dialect, driver, and DDL script are all
  MariaDB-specific.

## Design choices

- **Stateless JWT auth**, no refresh tokens, no server-side session/blacklist. `JwtAuthFilter`
  validates the bearer token on every request; there's no logout endpoint because logout is just
  "the client discards the token." This is standard for a simple API but means a compromised or
  leaked token remains valid until it expires (`jwt.expiration`, 24h by default).
- **DTOs at every boundary** (`dto/request`, `dto/response`) with MapStruct mappers, rather than
  exposing entities directly. Keeps JPA lazy-loading and persistence concerns out of the HTTP
  layer.
- **Service interfaces + impl split** (`service/interfaces`, `service/impelmentation`)

- **`ddl-auto=validate`.** The app expects the schema to already exist rather than auto-generating
  or migrating it.plain `db/schema.sql` was
  added (see the README) to make the "install" story concrete. `db/schema.sql` was hand-derived from the entity annotations rather than dumped from a real
  running database.
- **`BusinessException` + `@RestControllerAdvice`** for a uniform error shape on expected failures
  (404s modeled as bad requests, stock issues, duplicate username/email, etc.), with dedicated
  handlers for auth/permission errors.
