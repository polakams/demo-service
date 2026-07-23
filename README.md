# Library System Demo Service

A small **Library System** REST API you can run on your laptop. It stores a book catalog in an in-memory H2 database, exposes versioned CRUD endpoints (`/api/v1/...`), and ships with interactive OpenAPI (Swagger UI) docs titled **Library System**.

Use this project to learn Spring Boot layering (controller → service → repository), practice Git locally, and extend the API with new features from the exercise sheet. A root `.editorconfig` keeps Java/YAML/Markdown formatting consistent across editors.

## Prerequisites

- **Git**
- **JDK 25** (Java 25)

Verify:

```bash
java -version
git --version
```

You do **not** need to install Maven separately — this repo includes the Maven Wrapper (`mvnw` / `mvnw.cmd`).

## Clone and run

```bash
git clone https://github.com/polakams/demo-service.git
cd demo-service
./mvnw spring-boot:run
```

On Windows:

```bat
mvnw.cmd spring-boot:run
```

The first run downloads Maven and dependencies (needs network). When the app is up, you should see Spring Boot startup logs ending with the app listening on port 8080.

## Useful URLs

| What | URL |
| --- | --- |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |
| H2 console | http://localhost:8080/h2-console |
| Health | http://localhost:8080/actuator/health |
| Books API | http://localhost:8080/api/v1/books |

**H2 console JDBC settings**

- JDBC URL: `jdbc:h2:mem:library`
- User name: `sa`
- Password: *(leave empty)*

## Project layout

```
src/main/java/com/polakams/demoservice/
  DemoServiceApplication.java      # entry point
  book/
    Book.java                      # JPA entity
    BookRepository.java            # Spring Data repository
    BookService.java               # business logic
    BookController.java            # REST API (/api/v1/books)
    dto/                           # request/response DTOs
  common/
    GlobalExceptionHandler.java    # consistent error JSON
    ApiError.java
src/main/resources/
  application.yml
  data.sql                         # seed books
docs/
  exercises.md                     # learning tasks
  git-basics.md                    # Git fundamentals
```

**Request flow:** HTTP client → `BookController` → `BookService` → `BookRepository` → H2.

## Books API (reference)

| Method | Path | Behavior |
| --- | --- | --- |
| `GET` | `/api/v1/books` | List all books |
| `GET` | `/api/v1/books/{id}` | Get one book (404 if missing) |
| `POST` | `/api/v1/books` | Create a book (201; validates title/author/isbn/year) |
| `PUT` | `/api/v1/books/{id}` | Full update (404 if missing) |
| `DELETE` | `/api/v1/books/{id}` | Delete (204 / 404) |

Try these in Swagger UI without writing curl by hand.

## Documentation

- [docs/exercises.md](docs/exercises.md) — concrete learning tasks and scenarios
- [docs/git-basics.md](docs/git-basics.md) — checkout, branch, merge, fetch, pull, push

## Next steps

After you can run the app and list books in Swagger, pick a task from the [exercise sheet](docs/exercises.md): filter by author, add `PATCH`, introduce authors and loans, pagination, and stronger validation. Work on a **local feature branch** (see [Git basics](docs/git-basics.md)). Pushing or merging to shared `main` is **not** part of the learning flow for this project.

## Troubleshooting

**Wrong Java version**  
If `java -version` is not 25 (or compatible), install JDK 25 and ensure `JAVA_HOME` / `PATH` point at it before running `./mvnw`.

**Port 8080 already in use**  
Stop the other process, or temporarily change `server.port` in `src/main/resources/application.yml`.

**Tests**

```bash
./mvnw test
```

Good Luck Everyone with the Project!
