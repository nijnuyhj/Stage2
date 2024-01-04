package com.example.Week2.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequestDto {
    private String username;
    private String password;
}
