# bookvault-app

# BookVault - Library Management System

## How to Run

### Prerequisites
- Java 17+
- Maven 3.8+

### Start the Application
```bash
./mvnw spring-boot:run
```

The app runs on `http://localhost:8080`.

### H2 Console
Access the in-memory database at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:bookvaultdb`
- Username: `sa`
- Password: *(empty)*

---

## Seeded Credentials

| Role       | Email                        | Password   |
|------------|------------------------------|------------|
| LIBRARIAN  | librarian@bookvault.com      | password   |
| MEMBER     | member@bookvault.com         | password   |

### Login Example
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"librarian@bookvault.com","password":"password"}'
```

Use the returned JWT as: `Authorization: Bearer <token>`

---

## Design Decisions

### Layered Architecture
Controller → Service → Repository with strict separation. DTOs never leak into the persistence layer.

### Consistent API Envelope
All responses use `{ data, error, timestamp }`. On success `error` is null; on failure `data` is null.

### JWT Security
Stateless JWT with role-based access. `LIBRARIAN` has full access. `MEMBER` can view books, view their own loans, and return their own books.

### Caching Strategy
The book catalogue is cached using **Caffeine** with a 10-minute TTL and max 500 entries.
Cache is evicted on **any write operation** (create, update, delete) using `@CacheEvict(allEntries = true)`.
This is a **write-invalidate** strategy — simple, safe, and prevents stale data.

### Async Events
When a loan becomes overdue, a `LoanOverdueEvent` is published and handled asynchronously via `@EventListener` + `@Async`, so it never blocks the main thread.

### Scheduler
A `@Scheduled` cron job runs daily at midnight to scan for overdue loans and update their status.

### Dynamic Filtering
Book search uses **Spring Data Specifications** — no hardcoded query variants. Each filter (genre, author, available) is a composable predicate.

### Transactions
Borrow and return operations are fully `@Transactional`. If anything fails mid-way (e.g., saving the loan fails), the copy count change is rolled back.

---

## Trade-offs

| Decision | Trade-off |
|---|---|
| H2 in-memory | Easy setup, data lost on restart. Switching to PostgreSQL requires only changing `application.properties`. |
| `allEntries = true` cache eviction | Simpler than key-based eviction; fine for a book catalogue that changes infrequently. |
| No refresh tokens | Simpler auth flow; production would need refresh token rotation. |
| `data.sql` seeding | Quick to implement; Flyway/Liquibase would be better for production migrations. |

---

## What I'd Improve With More Time

- Add Flyway migrations instead of `data.sql`
- Add refresh token support
- Add rate limiting on auth endpoints
- Add pagination to all list endpoints
- Add an audit trail (created/updated timestamps) on all entities
- Add more granular MEMBER-level security (members can only see their own loans, not others')
- Add Docker + docker-compose setup
- Add OpenAPI/Swagger documentation
