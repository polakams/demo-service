package com.polakams.demoservice.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Request body for creating or fully replacing a book.
 */
public class BookRequest {

    @NotBlank(message = "title is required")
    @Size(max = 255, message = "title must be at most 255 characters")
    private String title;

    @NotBlank(message = "author is required")
    @Size(max = 255, message = "author must be at most 255 characters")
    private String author;

    @NotBlank(message = "isbn is required")
    @Size(max = 32, message = "isbn must be at most 32 characters")
    private String isbn;

    @NotNull(message = "publishedYear is required")
    @Positive(message = "publishedYear must be a positive number")
    private Integer publishedYear;

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
