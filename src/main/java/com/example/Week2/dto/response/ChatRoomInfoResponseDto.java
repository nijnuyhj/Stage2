package com.example.Week2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatRoomInfoResponseDto {
    private Long chatRoomId;
    private LocalDateTime lastChatTime;
    private ChatResponseDto lastChat;
    private Long receiverId;
    private String receiverUserName;
    private Boolean haveToRead; // 읽어야 할것이 있을때 true, 없으면 false
}
