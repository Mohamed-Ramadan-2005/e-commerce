# E-Commerce Platform API — Postman Collection

This collection lets you exercise every endpoint of the E-Commerce Platform API without writing any client code.

## 1. Import

1. Open Postman → **Import** → select `E-Commerce-Platform-API.postman_collection.json`.
2. Confirm `baseUrl` under the collection's **Variables** tab points to your running instance (defaults to `http://localhost:8080`).

## 2. Default seeded user

The database is seeded with one admin account out of the box:

| Field    | Value   |
|----------|---------|
| username | `admin` |
| password | `123`   |
| userId   | `1`     |
| role     | `ADMIN` |

Because this admin account already occupies `userId = 1`, **any new user you register through the `register` request will start at `userId = 2`** (and increment from there for each subsequent registration). Keep this in mind when setting the `userId` collection variable for requests like `promote` or `get user orders by id (admin)` — don't assume a newly registered user is `1`.

## 3. Typical flow

1. **`login`** (Authentication folder) — log in as `admin` / `123`. A test script automatically saves the returned JWT into the `{{token}}` collection variable, so you don't need to copy/paste it anywhere.
2. **`register`** — create a new regular user. Their `userId` will be `2` (or higher, if you've registered others before). Update the `userId` collection variable if you want to act on this specific user next.
3. **`promote`** *(admin only)* — while still authenticated as `admin`, promote the new user to `ADMIN` if needed, using `{{userId}}`.
4. **Products** *(admin only for write actions)* — `add product`, `update product`, `delete product` all require the admin's `{{token}}`. `get product` / `get all products` are public.
5. **Orders** — log in as the regular user (or admin) to get a fresh `{{token}}`, then:
   - `create order` places an order for whoever is currently authenticated.
   - `get my orders` returns only that user's orders.
   - `get all orders` and `get user orders by id (admin)` require the admin's token.
   - `update order status` requires admin.

## 4. Notes

- `{{token}}` is overwritten every time you run `login`, so if you switch between the admin and a regular user, just re-run `login` with the other credentials before hitting the next protected endpoint.
- `{{productId}}`, `{{userId}}`, and `{{orderId}}` are plain collection variables — update them manually in the **Variables** tab (or via a pre-request script) to point at whichever record you're testing against.
- No real secrets are stored in this collection; `{{token}}` starts empty and is populated at runtime.
