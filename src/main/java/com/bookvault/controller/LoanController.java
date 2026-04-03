package com.bookvault.controller;


import com.bookvault.dto.request.LoanRequest;
import com.bookvault.dto.response.*;
import com.bookvault.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<ApiResponse<LoanResponse>> borrowBook(@Valid @RequestBody LoanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(loanService.borrowBook(request)));
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<ApiResponse<LoanResponse>> returnBook(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(loanService.returnBook(id)));
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<ApiResponse<Page<LoanResponse>>> getOverdueLoans(
            @PageableDefault(size = 10, sort = "dueDate", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(loanService.getOverdueLoans(pageable)));
    }
}