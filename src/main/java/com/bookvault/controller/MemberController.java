package com.bookvault.controller;


import com.bookvault.dto.request.MemberRequest;
import com.bookvault.dto.response.*;
import com.bookvault.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final LoanService loanService;

    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getAllMembers() {
        return ResponseEntity.ok(ApiResponse.success(memberService.getAllMembers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponse>> getMemberById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(memberService.getMemberById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<ApiResponse<MemberResponse>> createMember(@Valid @RequestBody MemberRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(memberService.createMember(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMember(
            @PathVariable UUID id,
            @Valid @RequestBody MemberRequest request) {
        return ResponseEntity.ok(ApiResponse.success(memberService.updateMember(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable UUID id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> searchMembers(@RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.success(memberService.searchMembers(q)));
    }

    @GetMapping("/{id}/loans")
    public ResponseEntity<ApiResponse<List<LoanResponse>>> getMemberLoans(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(loanService.getMemberLoans(id)));
    }
}
