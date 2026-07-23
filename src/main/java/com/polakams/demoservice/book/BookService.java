package com.polakams.demoservice.book;

import com.polakams.demoservice.book.dto.BookRequest;
import com.polakams.demoservice.book.dto.BookResponse;
import com.polakams.demoservice.common.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Application service for book catalog operations.
 * <p>
 * Owns business rules and entity/DTO mapping; controllers stay thin.
 */
@Service
@Transactional
public class BookService {

    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Lists every book in the catalog.
     *
     * @return books as API responses, never {@code null}
     */
    @Transactional(readOnly = true)
    public List<BookResponse> findAll() {
        return bookRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Returns a single book by id.
     *
     * @param id book id
     * @return the matching book
     * @throws ResourceNotFoundException if no book exists with the given id
     */
    @Transactional(readOnly = true)
    public BookResponse findById(Long id) {
        return toResponse(getBookOrThrow(id));
    }

    /**
     * Creates and persists a new book from the request payload.
     *
     * @param request validated create payload
     * @return the persisted book
     */
    public BookResponse create(BookRequest request) {
        Book book = new Book();
        applyRequest(book, request);
        BookResponse response = toResponse(bookRepository.save(book));
        log.info("Created book id={} title={}", response.getId(), response.getTitle());
        return response;
    }

    /**
     * Fully replaces an existing book's catalog fields.
     *
     * @param id      book id to update
     * @param request validated update payload
     * @return the updated book
     * @throws ResourceNotFoundException if no book exists with the given id
     */
    public BookResponse update(Long id, BookRequest request) {
        Book book = getBookOrThrow(id);
        applyRequest(book, request);
        BookResponse response = toResponse(bookRepository.save(book));
        log.info("Updated book id={}", response.getId());
        return response;
    }

    /**
     * Deletes a book by id.
     *
     * @param id book id to delete
     * @throws ResourceNotFoundException if no book exists with the given id
     */
    public void delete(Long id) {
        bookRepository.delete(getBookOrThrow(id));
        log.info("Deleted book id={}", id);
    }

    private Book getBookOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
    }

    private void applyRequest(Book book, BookRequest request) {
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPublishedYear(request.getPublishedYear());
    }

    private BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublishedYear()
        );
    }
}
