package com.bookvault.repository;


import com.bookvault.entity.Book;
import com.bookvault.specification.BookSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void findByGenreSpecification_returnsMatchingBooks() {
        bookRepository.save(Book.builder()
                .isbn("978-0000000001")
                .title("Fantasy Book")
                .author("Author A")
                .genre("Fantasy")
                .totalCopies(2)
                .availableCopies(2)
                .build());

        bookRepository.save(Book.builder()
                .isbn("978-0000000002")
                .title("Sci-Fi Book")
                .author("Author B")
                .genre("Sci-Fi")
                .totalCopies(1)
                .availableCopies(1)
                .build());

        Specification<Book> spec = BookSpecification.hasGenre("Fantasy");
        List<Book> results = bookRepository.findAll(spec);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Fantasy Book");
    }
}
