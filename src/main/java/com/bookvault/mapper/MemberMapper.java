package com.bookvault.mapper;


import com.bookvault.entity.Member;
import com.bookvault.dto.request.MemberRequest;
import com.bookvault.dto.response.MemberResponse;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public Member toEntity(MemberRequest request) {
        return Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
    }

    public MemberResponse toResponse(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .membershipStatus(member.getMembershipStatus())
                .joinedAt(member.getJoinedAt())
                .build();
    }
}
