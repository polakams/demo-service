package com.polakams.demoservice.book.dto;

/**
 * API representation of a book returned to clients.
 */
public class BookResponse {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer publishedYear;

    /**
     * Creates an empty response for Jackson deserialization.
     */
    public BookResponse() {
    }

    /**
     * Creates a fully populated book response.
     *
     * @param id            book id
     * @param title         book title
     * @param author        book author
     * @param isbn          unique ISBN
     * @param publishedYear year of publication
     */
    public BookResponse(Long id, String title, String author, String isbn, Integer publishedYear) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }
}
