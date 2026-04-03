package com.bookvault.mapper;


import com.bookvault.entity.Loan;
import com.bookvault.dto.response.LoanResponse;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {

    public LoanResponse toResponse(Loan loan) {
        return LoanResponse.builder()
                .id(loan.getId())
                .bookId(loan.getBook().getId())
                .bookTitle(loan.getBook().getTitle())
                .memberId(loan.getMember().getId())
                .memberName(loan.getMember().getName())
                .borrowedAt(loan.getBorrowedAt())
                .dueDate(loan.getDueDate())
                .returnedAt(loan.getReturnedAt())
                .status(loan.getStatus())
                .build();
    }
}
