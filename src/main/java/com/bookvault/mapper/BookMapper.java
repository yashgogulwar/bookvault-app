package com.bookvault.mapper;



import com.bookvault.entity.Book;
import com.bookvault.dto.request.BookRequest;
import com.bookvault.dto.response.BookResponse;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toEntity(BookRequest request) {
        return Book.builder()
                .isbn(request.getIsbn())
                .title(request.getTitle())
                .author(request.getAuthor())
                .genre(request.getGenre())
                .totalCopies(request.getTotalCopies())
                .availableCopies(request.getTotalCopies())
                .build();
    }

    public BookResponse toResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .genre(book.getGenre())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .build();
    }
}
