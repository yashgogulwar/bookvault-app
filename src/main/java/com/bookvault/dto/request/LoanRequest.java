package com.bookvault.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoanRequest {

    @NotNull(message = "Book ID is required")
    private UUID bookId;

    @NotNull(message = "Member ID is required")
    private UUID memberId;
}
