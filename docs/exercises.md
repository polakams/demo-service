# Library System exercises

← Back to [README](../README.md)

Practice extending this Library System on your own machine. Create a feature branch, implement a task, verify with tests and Swagger, and keep your work local. **You do not need to push or merge to shared `main`.**

Books live under `/api/v1/books` (URL versioning). New resources in later tasks should follow the same `/api/v1/...` pattern unless you deliberately introduce a `v2`.

## How to work locally

1. Start from an up-to-date checkout of the project (see [README](../README.md)).
2. Create a feature branch (see [Git basics](git-basics.md)):
   ```bash
   git switch -c feature/filter-books
   ```
3. Implement the task (controller → service → repository / DTOs as needed).
4. Verify:
   ```bash
   ./mvnw test
   ./mvnw spring-boot:run
   ```
5. Open http://localhost:8080/swagger-ui.html and **Try it out**.

---

## Task 1 — Filter books

**Goal:** Support `GET /api/v1/books?author=` and/or `?title=` so callers can narrow the catalog.

**Hints:** Touch `BookController`, `BookService`, and possibly `BookRepository` (derived query methods or `@Query`). Keep listing all books when no filter params are sent.

**Acceptance criteria**

- Optional query params do not break existing clients.
- Matching is case-insensitive (or documented if not).
- Empty result returns `200` with `[]`.

**Verify:** In Swagger, call `GET /api/v1/books?author=Martin` and confirm only matching rows appear. Run `./mvnw test`.

---

## Task 2 — Partial update

**Goal:** Add `PATCH /api/v1/books/{id}` that updates only the fields present in the body.

**Hints:** New DTO (nullable fields) or `JsonNullable` / `Map` approach; service merges into the existing entity. Reuse 404 handling from `GlobalExceptionHandler`.

**Acceptance criteria**

- Omitting a field leaves the current value unchanged.
- Unknown id → `404` with the shared error JSON.
- Invalid values on provided fields → `400`.

**Verify:** Create a book, `PATCH` only `title`, `GET` and confirm author/isbn unchanged.

---

## Task 3 — Authors resource

**Goal:** New `/api/v1/authors` CRUD following the Books pattern (entity, repository, DTOs, service, controller, validation, OpenAPI annotations).

**Hints:** Mirror `book/` package structure under `author/`. Seed a few authors in `data.sql` (or a second SQL file if you prefer).

**Acceptance criteria**

- `GET/POST/PUT/DELETE` for authors with the same status codes as books.
- Swagger shows an **Authors** tag.
- At least one controller or integration test.

**Verify:** Create an author in Swagger, list authors, delete one.

---

## Task 4 — Relate book → author

**Goal:** Link each book to an author (foreign key or nested author field in responses).

**Hints:** JPA `@ManyToOne` on `Book`, or store `authorId` and resolve in the service. Update create/update DTOs and seed data.

**Acceptance criteria**

- Creating a book requires a valid author id (or equivalent).
- `GET` book responses include author identity (id and/or name).
- Deleting an author that still has books either cascades or returns a clear conflict — pick one and document it.

**Verify:** Create author → create book with that author → `GET` book shows the link.

---

## Task 5 — Loans / checkout

**Goal:** `/api/v1/loans` endpoints to borrow and return a book.

**Hints:** `Loan` entity with book id, borrower name, borrowed-at, returned-at. Endpoints such as `POST /api/v1/loans` (borrow) and `POST /api/v1/loans/{id}/return`.

**Acceptance criteria**

- Borrowing a book creates an open loan.
- Returning closes the loan.
- Cannot borrow a book that is already on an open loan (until Task 6 adds copies).

**Verify:** Borrow → try borrow again (expect error) → return → borrow succeeds again.

---

## Task 6 — Availability

**Goal:** Track copies / availability; reject a loan when none are left.

**Hints:** Add `totalCopies` / `availableCopies` on `Book`, or a separate inventory table. Decrement on borrow, increment on return.

**Acceptance criteria**

- Loan when available copies are `0` → `400` or `409` with a clear message.
- Successful return restores availability.
- List/get book shows availability.

**Verify:** Set a book to 1 copy, borrow it, confirm second borrow fails, return, borrow again.

---

## Task 7 — Pagination

**Goal:** Page + size (and optionally sort) on `GET /api/v1/books`.

**Hints:** Spring Data `Pageable` / `Page<Book>`; return a page object or a custom envelope (`content`, `page`, `size`, `totalElements`).

**Acceptance criteria**

- Default page size is documented and reasonable (e.g. 20).
- Out-of-range page returns empty content, not an error.
- Existing filter params (Task 1) still work with paging if you completed both.

**Verify:** Seed enough books (or create many via API), request `?page=0&size=2`, confirm two items and total count.

---

## Task 8 — Stronger validation

**Goal:** Unique ISBN, published-year range, and clear `400` responses.

**Hints:** DB unique constraint + service check; `@Min` / `@Max` on year; map `DataIntegrityViolationException` in `GlobalExceptionHandler` if needed.

**Acceptance criteria**

- Duplicate ISBN → `400` (or `409`) with a readable message.
- Year outside an agreed range (e.g. 1000–current year) → `400`.
- Error body still matches `ApiError` shape.

**Verify:** `POST` two books with the same ISBN; `POST` with year `3000`.

---

## More ideas

- Search across title + author with one `q` query param
- Soft-delete books instead of hard delete
- Actuator custom `info` with app version
- OpenAPI examples on request/response schemas
- Integration test covering loans + availability together

When you finish a task, skim the [README](../README.md) again for URLs and layout reminders.
