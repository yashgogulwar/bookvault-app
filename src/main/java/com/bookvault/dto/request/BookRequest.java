package com.bookvault.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BookRequest {

    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "978-\\d{10}", message = "ISBN must match format 978-XXXXXXXXXX")
    private String isbn;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    private String genre;

    @Min(value = 1, message = "Total copies must be at least 1")
    private int totalCopies;
}


