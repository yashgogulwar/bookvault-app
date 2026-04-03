package com.bookvault.service;




import com.bookvault.entity.Book;
import com.bookvault.dto.request.BookRequest;
import com.bookvault.dto.response.BookResponse;
import com.bookvault.exception.*;
import com.bookvault.mapper.BookMapper;
import com.bookvault.repository.BookRepository;
import com.bookvault.specification.BookSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    // ✅ READ - Served from cache after first call
    @Cacheable(value = "books", key = "'all_' + #genre + '_' + #author + '_' + #available")
    public List<BookResponse> getAllBooks(String genre, String author, Boolean available) {
        log.info("[CACHE MISS] Fetching books from DB - genre={}, author={}, available={}", genre, author, available);

        Specification<Book> spec = Specification.allOf(
                BookSpecification.hasGenre(genre),
                BookSpecification.hasAuthor(author),
                BookSpecification.isAvailable(available)
        );

        return bookRepository.findAll(spec).stream()
                .map(bookMapper::toResponse)
                .collect(Collectors.toList());
    }

    // ✅ READ SINGLE - No cache needed for single book fetch
    public BookResponse getBookById(UUID id) {
        log.info("[DB] Fetching single book by id={}", id);
        return bookMapper.toResponse(findBookById(id));
    }

    // ✅ CREATE - Evicts ALL cache entries so fresh data is fetched next time
    @Transactional
    @CacheEvict(value = "books", allEntries = true)
    public BookResponse createBook(BookRequest request) {
        log.info("[CACHE EVICT] Creating book - evicting all cache entries");
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException("Book with ISBN " + request.getIsbn() + " already exists");
        }
        Book book = bookMapper.toEntity(request);
        BookResponse response = bookMapper.toResponse(bookRepository.save(book));
        log.info("[CACHE EVICT DONE] Book created with id={}", response.getId());
        return response;
    }

    // ✅ UPDATE - Evicts ALL cache entries so stale data is not served
    @Transactional
    @CacheEvict(value = "books", allEntries = true)
    public BookResponse updateBook(UUID id, BookRequest request) {
        log.info("[CACHE EVICT] Updating book id={} - evicting all cache entries", id);
        Book book = findBookById(id);
        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setGenre(request.getGenre());
        int diff = request.getTotalCopies() - book.getTotalCopies();
        book.setTotalCopies(request.getTotalCopies());
        book.setAvailableCopies(Math.max(0, book.getAvailableCopies() + diff));
        BookResponse response = bookMapper.toResponse(bookRepository.save(book));
        log.info("[CACHE EVICT DONE] Book updated with id={}", response.getId());
        return response;
    }

    // ✅ DELETE - Evicts ALL cache entries so deleted book is not served from cache
    @Transactional
    @CacheEvict(value = "books", allEntries = true)
    public void deleteBook(UUID id) {
        log.info("[CACHE EVICT] Deleting book id={} - evicting all cache entries", id);
        findBookById(id);
        bookRepository.deleteById(id);
        log.info("[CACHE EVICT DONE] Book deleted with id={}", id);
    }

    public Book findBookById(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }
}