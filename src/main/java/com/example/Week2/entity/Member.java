package com.example.Week2.entity;

import com.example.Week2.dto.request.LoginRequestDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    public Member(String username, String password){
        this.username = username;
        this.password = password;
    }


    public Member(LoginRequestDto dto) {
        this.password = dto.getPassword();
        this.username = dto.getUsername();
    }
}
