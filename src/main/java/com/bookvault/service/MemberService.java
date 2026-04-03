package com.bookvault.service;


import com.bookvault.entity.Member;
import com.bookvault.dto.request.MemberRequest;
import com.bookvault.dto.response.MemberResponse;
import com.bookvault.exception.*;
import com.bookvault.mapper.MemberMapper;
import com.bookvault.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(memberMapper::toResponse)
                .collect(Collectors.toList());
    }

    public MemberResponse getMemberById(UUID id) {
        return memberMapper.toResponse(findMemberById(id));
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Member with email " + request.getEmail() + " already exists");
        }
        Member member = memberMapper.toEntity(request);
        return memberMapper.toResponse(memberRepository.save(member));
    }

    @Transactional
    public MemberResponse updateMember(UUID id, MemberRequest request) {
        Member member = findMemberById(id);
        member.setName(request.getName());
        member.setEmail(request.getEmail());
        if (request.getMembershipStatus() != null) {
            member.setMembershipStatus(request.getMembershipStatus());
        }
        return memberMapper.toResponse(memberRepository.save(member));
    }

    @Transactional
    public void deleteMember(UUID id) {
        findMemberById(id);
        memberRepository.deleteById(id);
    }

    public List<MemberResponse> searchMembers(String q) {
        return memberRepository.searchByNameOrEmail(q).stream()
                .map(memberMapper::toResponse)
                .collect(Collectors.toList());
    }

    public Member findMemberById(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
    }
}
