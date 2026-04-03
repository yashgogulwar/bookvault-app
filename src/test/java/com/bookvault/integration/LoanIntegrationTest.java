package com.bookvault.integration;


import com.bookvault.entity.*;
import com.bookvault.entity.enums.*;
import com.bookvault.dto.request.LoanRequest;
import com.bookvault.dto.response.LoanResponse;
import com.bookvault.repository.*;
import com.bookvault.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class LoanIntegrationTest {

    @Autowired private LoanService loanService;
    @Autowired private BookRepository bookRepository;
    @Autowired private MemberRepository memberRepository;

    private Book book;
    private Member member;

    @BeforeEach
    void setUp() {
        book = bookRepository.save(Book.builder()
                .isbn("978-1234567890")
                .title("Test Book")
                .author("Test Author")
                .genre("Test")
                .totalCopies(2)
                .availableCopies(2)
                .build());

        member = memberRepository.save(Member.builder()
                .email("test@integration.com")
                .name("Test Member")
                .membershipStatus(MembershipStatus.ACTIVE)
                .build());
    }

    @Test
    void borrowAndReturn_fullFlow_success() {
        LoanRequest request = new LoanRequest(book.getId(), member.getId());

        LoanResponse loan = loanService.borrowBook(request);
        assertThat(loan.getStatus()).isEqualTo(LoanStatus.ACTIVE);

        Book updatedBook = bookRepository.findById(book.getId()).orElseThrow();
        assertThat(updatedBook.getAvailableCopies()).isEqualTo(1);

        LoanResponse returned = loanService.returnBook(loan.getId());
        assertThat(returned.getStatus()).isEqualTo(LoanStatus.RETURNED);
        assertThat(returned.getReturnedAt()).isNotNull();

        Book finalBook = bookRepository.findById(book.getId()).orElseThrow();
        assertThat(finalBook.getAvailableCopies()).isEqualTo(2);
    }

    @Test
    void borrowBook_whenNoCopiesAvailable_throwsException() {
        book.setAvailableCopies(0);
        bookRepository.save(book);

        LoanRequest request = new LoanRequest(book.getId(), member.getId());

        assertThatThrownBy(() -> loanService.borrowBook(request))
                .hasMessageContaining("No available copies");
    }
}
