package com.bookvault.dto.response;


import com.bookvault.entity.enums.MembershipStatus;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MemberResponse {
    private UUID id;
    private String email;
    private String name;
    private MembershipStatus membershipStatus;
    private LocalDateTime joinedAt;
}
