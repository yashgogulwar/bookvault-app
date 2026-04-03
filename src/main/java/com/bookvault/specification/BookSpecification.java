package com.bookvault.specification;



import com.bookvault.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Book> hasGenre(String genre) {
        return (root, query, cb) ->
                genre == null ? cb.conjunction() : cb.equal(cb.lower(root.get("genre")), genre.toLowerCase());
    }

    public static Specification<Book> hasAuthor(String author) {
        return (root, query, cb) ->
                author == null ? cb.conjunction() : cb.like(cb.lower(root.get("author")), "%" + author.toLowerCase() + "%");
    }

    public static Specification<Book> isAvailable(Boolean available) {
        return (root, query, cb) ->
                (available == null || !available) ? cb.conjunction() : cb.greaterThan(root.get("availableCopies"), 0);
    }
}