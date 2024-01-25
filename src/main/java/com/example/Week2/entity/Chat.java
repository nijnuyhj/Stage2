package com.example.Week2.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class Chat {
    @Id
    private String id;
    private String uuid;
    private String message;
    private Long senderId; // 채팅을 보내는 유저의 아이디
    private Long receiverId; // 채팅을 받는 유저의 아이디
    private Long chatRoomId;
    private LocalDateTime createdAt;
    private Boolean readStatus = false; // 채팅의 읽음 , 읽지않음 표시

    private Chat(String message, Long senderId ,Long receiverId, Long chatRoomId, LocalDateTime createdAt) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.chatRoomId = chatRoomId;
        this.createdAt = createdAt;
        this.uuid = UUID.randomUUID().toString().substring(0,8);
    }

    public Chat() {

    }

    public static Chat of(String message, Long senderId, Long receiverId, Long chatRoomId, LocalDateTime createdAt){
        return new Chat(message,senderId,receiverId,chatRoomId,createdAt);
    }

    public void updateReadStatus(){
        this.readStatus = true;
    }
}