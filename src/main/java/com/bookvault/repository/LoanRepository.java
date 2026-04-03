package com.bookvault.repository;


import com.bookvault.entity.Loan;
import com.bookvault.entity.enums.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface LoanRepository extends JpaRepository<Loan, UUID> {

    List<Loan> findByMemberId(UUID memberId);

    long countByMemberIdAndStatus(UUID memberId, LoanStatus status);

    @Query("SELECT l FROM Loan l WHERE l.dueDate < :now AND l.status = 'ACTIVE'")
    Page<Loan> findOverdueLoans(@Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT l FROM Loan l WHERE l.dueDate < :now AND l.status = 'ACTIVE'")
    List<Loan> findOverdueLoansForUpdate(@Param("now") LocalDateTime now);
}
