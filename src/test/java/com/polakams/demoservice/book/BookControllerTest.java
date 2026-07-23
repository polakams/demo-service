package com.polakams.demoservice.book;

import com.polakams.demoservice.common.GlobalExceptionHandler;
import com.polakams.demoservice.common.ResourceNotFoundException;
import com.polakams.demoservice.book.dto.BookResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class)
@Import(GlobalExceptionHandler.class)
class BookControllerTest {

    private static final String VALID_BOOK_JSON = """
            {
              "title": "Refactoring",
              "author": "Martin Fowler",
              "isbn": "9780134757599",
              "publishedYear": 2018
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    void listBooks_returnsOk() throws Exception {
        when(bookService.findAll()).thenReturn(List.of(
                new BookResponse(1L, "Clean Code", "Robert C. Martin", "9780132350884", 2008)
        ));

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Clean Code"));
    }

    @Test
    void getBook_returnsOk() throws Exception {
        when(bookService.findById(1L)).thenReturn(
                new BookResponse(1L, "Clean Code", "Robert C. Martin", "9780132350884", 2008)
        );

        mockMvc.perform(get("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("9780132350884"));
    }

    @Test
    void getBook_returns404WhenMissing() throws Exception {
        when(bookService.findById(99L)).thenThrow(new ResourceNotFoundException("Book not found with id 99"));

        mockMvc.perform(get("/api/v1/books/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Book not found with id 99"));
    }

    @Test
    void createBook_returns201() throws Exception {
        when(bookService.create(any())).thenReturn(
                new BookResponse(10L, "Refactoring", "Martin Fowler", "9780134757599", 2018)
        );

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BOOK_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void createBook_returns400WhenInvalid() throws Exception {
        String invalidJson = """
                {
                  "title": "",
                  "author": "",
                  "isbn": "",
                  "publishedYear": -1
                }
                """;

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void updateBook_returnsOk() throws Exception {
        when(bookService.update(eq(1L), any())).thenReturn(
                new BookResponse(1L, "Refactoring", "Martin Fowler", "9780134757599", 2018)
        );

        mockMvc.perform(put("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BOOK_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteBook_returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBook_returns404WhenMissing() throws Exception {
        doThrow(new ResourceNotFoundException("Book not found with id 99")).when(bookService).delete(99L);

        mockMvc.perform(delete("/api/v1/books/99"))
                .andExpect(status().isNotFound());
    }
}
