package com.example.Week2.service;

import com.example.Week2.dto.request.MemberRequestDto;
import com.example.Week2.entity.Member;
import com.example.Week2.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public void signUp(MemberRequestDto memberRequestDto){
        Member member = memberRequestDto.toEntity();
        memberRepository.save(member);
    }
}
