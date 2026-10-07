# ServletCrud

A simple **User CRUD REST API** built with **Jakarta Servlets** (Jakarta EE 10 /
`jakarta.servlet-api` 6.1) and packaged as a WAR. Users are stored in an
in-memory map — no database required — which makes it a clean starting point for
learning the raw Servlet API without a framework like Spring.

## Features

- `GET`, `POST`, `PUT`, `DELETE` on a single `/users` endpoint
- JSON responses with proper status codes (`200`, `201`, `400`, `404`, `409`)
- Thread-safe in-memory store (`ConcurrentHashMap`)
- No external framework — just the Servlet API

## Tech stack

| Layer      | Choice                              |
|------------|-------------------------------------|
| Language   | Java 25                             |
| API        | Jakarta Servlet 6.1 (`provided`)    |
| Build      | Maven (`war` packaging)             |
| Runtime    | Any Jakarta EE 10 servlet container (Tomcat 11+, Jetty 12) |

## Project structure

```
src/main/java/org/example
├── model/User.java        # User entity (id, name, email, mobile)
├── service/UserService.java  # In-memory store + CRUD logic
└── servlet/UserServlet.java  # HTTP layer, maps requests to the service
```

## Build & run

> Requires JDK 25 and a Jakarta EE 10 servlet container (e.g. Tomcat 11).

```bash
# Build the WAR (produces target/crud-app.war)
mvn clean package

# Deploy target/crud-app.war to your servlet container,
# then the API is available at:
#   http://localhost:8080/crud-app/users
```

## API reference

Base path: `/users`

### Create a user — `POST /users`

Parameters (form-encoded): `id`, `name`, `email`, `mobile`

```bash
curl -X POST "http://localhost:8080/crud-app/users" \
  -d "id=1&name=Jawad&email=jawad@example.com&mobile=03001234567"
```

`201 Created`
```json
{ "message": "User added successfully" }
```

### Get all users — `GET /users`

```bash
curl "http://localhost:8080/crud-app/users"
```

`200 OK`
```json
[
  { "id": 1, "name": "Jawad", "email": "jawad@example.com", "mobile": "03001234567" }
]
```

### Get a user by id — `GET /users?id=1`

```bash
curl "http://localhost:8080/crud-app/users?id=1"
```

`200 OK` with the user, or `404 Not Found` if it does not exist.

### Update a user — `PUT /users`

Parameters: `id`, `name`, `email`, `mobile`

```bash
curl -X PUT "http://localhost:8080/crud-app/users" \
  -d "id=1&name=Jawad%20Yousaf&email=new@example.com&mobile=03009999999"
```

`200 OK` on success, `404 Not Found` if the id does not exist.

### Delete a user — `DELETE /users?id=1`

```bash
curl -X DELETE "http://localhost:8080/crud-app/users?id=1"
```

`200 OK` on success, `404 Not Found` if the id does not exist.

## Notes & limitations

- Data lives in memory and is lost when the container restarts.
- JSON is built by hand (no Jackson/Gson) to keep dependencies minimal.
- Intended as a learning project for the plain Servlet API.

## Possible next steps

- Persist users in a real database (JDBC / JPA)
- Replace hand-built JSON with Jackson or Gson
- Add input validation (email format, unique email) and unit tests
- Parse JSON request bodies instead of form parameters
