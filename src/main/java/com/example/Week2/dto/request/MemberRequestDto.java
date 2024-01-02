package com.example.Week2.dto.request;

import com.example.Week2.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberRequestDto {
    private String username;
    private String password;
    private String confirm;

    public Member toEntity(){
        return Member.builder()
                .username(username)
                .password(password)
                .build();
    }
}
