# E-Commerce API

A Spring Boot REST API for a small e-commerce platform: user registration/login (JWT), a product
catalog organized into categories (with search and pagination), and order placement with stock
tracking. Two roles are supported, `USER` and `ADMIN`.

See [`DESIGN.md`](./DESIGN.md) for assumptions, design choices, and known limitations.

## Prerequisites

- JDK 17+
- MariaDB 10.x (or compatible) running locally or reachable over the network
- Maven is not required to be installed separately — the project ships the Maven Wrapper
  (`./mvnw` / `mvnw.cmd`)

## 1. Create the database and schema

```bash
mysql -u root -p -e "CREATE DATABASE ecommerce;"
mysql -u root -p ecommerce < db/schema.sql
```

The app runs with `spring.jpa.hibernate.ddl-auto=validate`, meaning Hibernate checks the schema
against the entity mappings at startup but never creates or alters tables itself. `db/schema.sql`
contains the DDL that matches the current entities — run it once before the first start, and
again (manually) whenever an entity changes shape.
## 2. Configure environment variables (.env)

The application securely loads secrets and database configuration from a `.env` file using `spring-dotenv`.

### Create the `.env` file

In the root directory of the project (next to `pom.xml`), create a file named exactly:

```
.env
```

Then add the following variables:

```env
# Database Configuration
DB_URL=jdbc:mariadb://localhost:3306/ecommerce
DB_USERNAME=root
DB_PASSWORD=your_mariadb_password

# JWT Configuration (Base64 string, >=32 bytes when decoded)
JWT_SECRET=your_generated_jwt_secret
JWT_EXPIRATION=86400000
```

### Generate a secure JWT secret

Run the following command:

```bash
openssl rand -base64 32
```

Then copy the output and set it as your `JWT_SECRET`.

---

## 3. Note on CORS

`CorsConfig` currently allows only `http://localhost:4200` (a local Angular dev server) as a
cross-origin caller, with `GET`/`POST`/`PUT`/`PATCH`/`DELETE`/`OPTIONS` and credentials enabled.
If you're calling the API from a frontend running on a different origin, update the
`allowedOrigins(...)` value in `CorsConfig` — server-to-server calls, curl, and Postman are
unaffected either way, since CORS only applies to browser requests.

## 4. Run the application

```bash
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`.

On first startup, `AppStartUp` seeds the `USER` and `ADMIN` roles and, if no `admin` user exists
yet, creates one (`username: admin`, `password 123`).
**Change or remove this account before deploying anywhere reachable by others.**

## 5. Build a jar

```bash
./mvnw clean package
java -jar target/e-commerce-0.0.1-SNAPSHOT.jar
```

## 6. Run tests

```bash
./mvnw test
```

Tests are unit tests only (Mockito-based service tests, `@WebMvcTest` controller tests with
security filters disabled) — no database is required to run them.

## API documentation

With the app running:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Raw OpenAPI spec: `http://localhost:8080/openapi.yaml`

A ready-to-import Postman collection is also included at
`src/main/resources/postman/E-Commerce-Platform-API.postman_collection.json`, with its own
[usage notes](./src/main/resources/postman/README.md).

## Quick API tour

| Method | Path                        | Auth   | Description                                              |
|--------|-----------------------------|--------|------------------------------------------------------------|
| POST   | `/users/register`           | Public | Register a new user (`USER` role)                          |
| POST   | `/users/login`               | Public | Log in, returns a JWT                                      |
| PATCH  | `/users/{userId}/promote`   | ADMIN  | Promote a user to `ADMIN`                                  |
| GET    | `/products`                  | Public | List products (paginated)                                  |
| GET    | `/products/{id}`             | Public | Get one product                                             |
| GET    | `/products/category/{id}`   | Public | List products in a category (paginated)                    |
| GET    | `/products/search/{name}`   | Public | Search products by name, case-insensitive (paginated)      |
| POST   | `/products`                   | ADMIN  | Create a product                                            |
| PUT    | `/products/{id}`             | ADMIN  | Update a product                                            |
| DELETE | `/products/{id}`             | ADMIN  | Delete a product                                            |
| GET    | `/categories`                 | Public | List all categories                                         |
| GET    | `/categories/{id}`           | Public | Get one category                                            |
| POST   | `/categories`                 | ADMIN  | Create a category                                           |
| PUT    | `/categories/{id}`           | ADMIN  | Update a category                                           |
| DELETE | `/categories/{id}`           | ADMIN  | Delete a category                                           |
| POST   | `/orders`                     | USER   | Place an order                                              |
| GET    | `/orders/my-orders`           | USER   | View your own orders                                        |
| GET    | `/orders`                     | ADMIN  | List all orders                                             |
| GET    | `/orders/user/{userId}`       | ADMIN  | List a specific user's orders                               |
| PATCH  | `/orders/{id}/status`         | ADMIN  | Update an order's status                                    |

Authenticated requests use `Authorization: Bearer <token>`.

The three paginated `GET /products*` endpoints accept `page` (default `0`) and `size`
(default `10`) query parameters, e.g. `GET /products?page=1&size=20`. They return a Spring
`Page<T>` object (`content`, `totalElements`, `totalPages`, `number`, ...), not a bare array —
update any client that expects a plain JSON list from an earlier version of this API.

Each product belongs to exactly one category (`categoryId` on create/update requests,
`categoryName` in responses) rather than many categories at once — see [`DESIGN.md`](./DESIGN.md)
for the reasoning.

## Error responses

Errors come back as JSON with a consistent `{ "status", "error", "message" }` shape (field-level
validation errors instead return a `{ "field": "message" }` map). Status codes in use:

| Status | When |
|--------|------|
| `400 Bad Request` | Request validation failures; business-rule violations (insufficient stock, duplicate username/email, promoting an already-ADMIN user) |
| `401 Unauthorized` | Missing, invalid, or expired credentials |
| `403 Forbidden` | Authenticated, but the account's role doesn't allow the action |
| `404 Not Found` | The requested product, category, user, or order doesn't exist |
| `500 Internal Server Error` | Unexpected server-side errors |
