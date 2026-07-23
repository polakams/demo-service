package com.polakams.demoservice.book;

import com.polakams.demoservice.book.dto.BookRequest;
import com.polakams.demoservice.book.dto.BookResponse;
import com.polakams.demoservice.common.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for the book catalog at {@code /api/v1/books}.
 * <p>
 * Delegates all business logic to {@link BookService}.
 */
@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "Books", description = "Library catalog — manage books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Lists all books in the catalog.
     *
     * @return every book as API responses
     */
    @GetMapping
    @Operation(summary = "List all books")
    @ApiResponse(responseCode = "200", description = "Books returned")
    public List<BookResponse> listBooks() {
        return bookService.findAll();
    }

    /**
     * Returns one book by id.
     *
     * @param id book id
     * @return the matching book
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a book by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public BookResponse getBook(@PathVariable Long id) {
        return bookService.findById(id);
    }

    /**
     * Creates a book from a validated request body.
     *
     * @param request create payload
     * @return {@code 201} with the created book
     */
    @PostMapping
    @Operation(summary = "Add a book to the catalog")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book created"),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
        BookResponse created = bookService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Fully replaces an existing book.
     *
     * @param id      book id
     * @param request update payload
     * @return the updated book
     */
    @PutMapping("/{id}")
    @Operation(summary = "Replace a book")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public BookResponse updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        return bookService.update(id, request);
    }

    /**
     * Removes a book from the catalog.
     *
     * @param id book id
     * @return {@code 204} when deleted
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove a book from the catalog")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book deleted"),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
