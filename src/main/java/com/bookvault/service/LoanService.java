package com.bookvault.service;



import com.bookvault.entity.*;
import com.bookvault.entity.enums.*;
import com.bookvault.dto.request.LoanRequest;
import com.bookvault.dto.response.LoanResponse;
import com.bookvault.event.LoanOverdueEvent;
import com.bookvault.exception.*;
import com.bookvault.mapper.LoanMapper;
import com.bookvault.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookService bookService;
    private final MemberService memberService;
    private final LoanMapper loanMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public LoanResponse borrowBook(LoanRequest request) {
        Book book = bookService.findBookById(request.getBookId());
        Member member = memberService.findMemberById(request.getMemberId());

        if (member.getMembershipStatus() == MembershipStatus.SUSPENDED) {
            throw new BusinessException("Member is suspended and cannot borrow books");
        }

        if (book.getAvailableCopies() <= 0) {
            throw new BusinessException("No available copies of this book");
        }

        long activeLoans = loanRepository.countByMemberIdAndStatus(member.getId(), LoanStatus.ACTIVE);
        if (activeLoans >= 3) {
            throw new BusinessException("Member already has 3 active loans. Return a book first.");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);

        Loan loan = Loan.builder()
                .book(book)
                .member(member)
                .build();

        return loanMapper.toResponse(loanRepository.save(loan));
    }

    @Transactional
    public LoanResponse returnBook(UUID loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + loanId));

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new BusinessException("This book has already been returned");
        }

        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnedAt(LocalDateTime.now());
        loan.getBook().setAvailableCopies(loan.getBook().getAvailableCopies() + 1);

        return loanMapper.toResponse(loanRepository.save(loan));
    }

    public Page<LoanResponse> getOverdueLoans(Pageable pageable) {
        return loanRepository.findOverdueLoans(LocalDateTime.now(), pageable)
                .map(loanMapper::toResponse);
    }

    public List<LoanResponse> getMemberLoans(UUID memberId) {
        memberService.findMemberById(memberId);
        return loanRepository.findByMemberId(memberId).stream()
                .map(loanMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateOverdueStatuses() {
        List<Loan> overdueLoans = loanRepository.findOverdueLoansForUpdate(LocalDateTime.now());
        overdueLoans.forEach(loan -> {
            loan.setStatus(LoanStatus.OVERDUE);
            eventPublisher.publishEvent(new LoanOverdueEvent(this, loan));
        });
        loanRepository.saveAll(overdueLoans);
    }
}