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
- **Service interfaces + impl split** (`service/interfaces`, `service/implementation`)

- **`ddl-auto=validate`.** The app expects the schema to already exist rather than auto-generating
  or migrating it.plain `db/schema.sql` was
  added (see the README) to make the "install" story concrete. `db/schema.sql` was hand-derived from the entity annotations rather than dumped from a real
  running database.
- **Two distinct exception types, both handled by `@RestControllerAdvice`**, so the HTTP status
  actually matches what went wrong: `ResourceNotFoundException` → `404` for "this
  product/category/user/order doesn't exist," and `BusinessException` → `400` for rule violations
  on an otherwise-valid request (insufficient stock, duplicate username/email, promoting an
  already-ADMIN user). `BadCredentialsException` → `401` and `AccessDeniedException` → `403` get
  their own handlers too, so every error path returns a status a client can branch on rather than
  everything collapsing into "bad request."
- **Product listing endpoints are paginated** (`GET /products`, `/products/category/{id}`,
  `/products/search/{name}`) via Spring Data's `Pageable`, defaulting to a page size of 10. This
  is a breaking response-shape change from a bare JSON array to a `Page<T>` wrapper — acceptable
  here since there's no external consumer yet to break, but worth flagging if this API ever
  ships a v1 client.
