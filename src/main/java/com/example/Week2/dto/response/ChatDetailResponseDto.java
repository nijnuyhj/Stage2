package com.example.Week2.dto.response;

import com.example.Week2.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatDetailResponseDto {
    private String senderUserName;
//    private String senderProfileImg;
    private String message;
    private LocalDateTime createdAt;
    private Long senderId;

    public static ChatDetailResponseDto from(ChatResponseDto chat, Member member){
        return new ChatDetailResponseDto(member.getUsername(),
//                users.getProfileImg(),
                chat.getMessage(),
                chat.getCreatedAt(),
                member.getId());
    }
}

