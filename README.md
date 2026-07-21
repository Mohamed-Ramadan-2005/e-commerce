# E-Commerce API

A Spring Boot REST API for a small e-commerce platform: user registration/login (JWT), product
catalog management, and order placement with stock tracking. Two roles are supported, `USER` and
`ADMIN`.

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

## 3. Run the application

```bash
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`.

On first startup, `AppStartUp` seeds the `USER` and `ADMIN` roles and, if no `admin` user exists
yet, creates one (`username: admin`, `password 123`).
**Change or remove this account before deploying anywhere reachable by others.**

## 4. Build a jar

```bash
./mvnw clean package
java -jar target/e-commerce-0.0.1-SNAPSHOT.jar
```

## 5. Run tests

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

| Method | Path                     | Auth        | Description                          |
|--------|--------------------------|-------------|---------------------------------------|
| POST   | `/users/register`        | Public      | Register a new user (`USER` role)     |
| POST   | `/users/login`           | Public      | Log in, returns a JWT                 |
| PATCH  | `/users/{userId}/promote`| ADMIN       | Promote a user to `ADMIN`             |
| GET    | `/products`               | Public      | List products                         |
| GET    | `/products/{id}`          | Public      | Get one product                       |
| POST   | `/products`                | ADMIN       | Create a product                      |
| PUT    | `/products/{id}`          | ADMIN       | Update a product                      |
| DELETE | `/products/{id}`          | ADMIN       | Delete a product                      |
| POST   | `/orders`                  | USER        | Place an order                        |
| GET    | `/orders/my-orders`        | USER        | View your own orders                  |
| GET    | `/orders`                  | ADMIN       | List all orders                       |
| GET    | `/orders/user/{userId}`    | ADMIN       | List a specific user's orders         |
| PATCH  | `/orders/{id}/status`      | ADMIN       | Update an order's status              |

Authenticated requests use `Authorization: Bearer <token>`.
