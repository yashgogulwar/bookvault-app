package com.bookvault.dto.response;


import com.bookvault.entity.enums.LoanStatus;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoanResponse {
    private UUID id;
    private UUID bookId;
    private String bookTitle;
    private UUID memberId;
    private String memberName;
    private LocalDateTime borrowedAt;
    private LocalDateTime dueDate;
    private LocalDateTime returnedAt;
    private LoanStatus status;
}
