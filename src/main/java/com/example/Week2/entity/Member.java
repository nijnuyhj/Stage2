package com.example.Week2.entity;

import com.example.Week2.dto.request.LoginRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="member_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "member")
    private List<Post> post = new ArrayList<>();

    @OneToMany(mappedBy ="member")
    private List<Comment> comment = new ArrayList<>();
    public Member(String username, String password){
        this.username = username;
        this.password = password;
    }


    public Member(LoginRequestDto dto) {
        this.password = dto.getPassword();
        this.username = dto.getUsername();
    }

    public String getUsername() {
        return username;
    }

}
