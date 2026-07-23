package com.polakams.demoservice.book;

import com.polakams.demoservice.book.dto.BookRequest;
import com.polakams.demoservice.book.dto.BookResponse;
import com.polakams.demoservice.common.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void findAll_mapsEntitiesToResponses() {
        Book book = new Book("Clean Code", "Robert C. Martin", "9780132350884", 2008);
        book.setId(1L);
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookResponse> result = bookService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getTitle()).isEqualTo("Clean Code");
    }

    @Test
    void findById_throwsWhenMissing() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_persistsAndReturnsResponse() {
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book saved = invocation.getArgument(0);
            saved.setId(42L);
            return saved;
        });

        BookRequest request = new BookRequest();
        request.setTitle("Refactoring");
        request.setAuthor("Martin Fowler");
        request.setIsbn("9780134757599");
        request.setPublishedYear(2018);

        BookResponse response = bookService.create(request);

        assertThat(response.getId()).isEqualTo(42L);
        assertThat(response.getIsbn()).isEqualTo("9780134757599");
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void delete_throwsWhenMissing() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
