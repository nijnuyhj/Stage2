package com.example.Week2.dto.response;

import com.example.Week2.entity.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberResponseDto {
    private Long id;
    private String username;


    public MemberResponseDto(Member member){
        this.id = member.getId();
        this.username = member.getUsername();
    }

}