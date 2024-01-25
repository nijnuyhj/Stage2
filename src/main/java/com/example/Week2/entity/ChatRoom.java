package com.example.Week2.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ChatRoom{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private Member host;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    private Member guest;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime hostEntryTime; // 5/28 20시 10분 -> 6/3 20시 50분
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime guestEntryTime; // 5/28 20시 10분
    private ChatRoom(Member host , Member guest,LocalDateTime time) {
        this.host = host;
        this.guest = guest;
        this.hostEntryTime = time;
        this.guestEntryTime = time;
    }
    public static ChatRoom of(Member host,Member guest,LocalDateTime time){
        return new ChatRoom(host,guest,time);
    }
    public void updateHostEntryTime(LocalDateTime time){
        this.hostEntryTime = time;
    }
    public void updateGuestEntryTime(LocalDateTime time){
        this.guestEntryTime = time;
    }
}