package com.example.Week2.controller;

import com.example.Week2.dto.request.ChatRequestDto;
import com.example.Week2.dto.response.ChatDetailResponseDto;
import com.example.Week2.dto.response.ChatResponseDto;
import com.example.Week2.dto.response.ChatRoomInfoResponseDto;
import com.example.Week2.dto.response.ChatRoomResponseDto;
import com.example.Week2.security.UserDetailsImpl;
import com.example.Week2.service.ChatService;
import com.example.Week2.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Queue;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate msgOperation;
    private final NotificationService notificationService;

//    @GetMapping("room/{receiverId}")    //채팅방 읽음
//    public ResponseEntity<RoomResponseDto> enterChatRoom(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long receiverId){
//        return chatService.enterRoom(userDetails.getMember(),receiverId);
//    }
    @GetMapping("/chat/list")    //채팅방 리스트
    public ResponseEntity<Queue<ChatRoomInfoResponseDto>> findAllChatRoomByUser(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return chatService.findAllChatRoom(userDetails.getMember());
    }

     /*
    채팅방을 생성 -> 채팅방이 없는 상태라면
    /sub/chat/room으로 먼저 채팅방 Id 를 보냄
    /sub/chat/room/{chatRoomId} 에 chatRequestDto를 보낸 후
    /sub/chat{receiverId}에 알림을 보냄
    */
    @MessageMapping("/chat/message")    //pub/chat/message
    public void enterChatRoom(ChatRequestDto chatRequestDto){
        ChatResponseDto chatResponseDto = chatService.saveChat(chatRequestDto);
        ChatDetailResponseDto chatDetailResponseDto = chatService.createChatDetailResponseDto(chatResponseDto);
        msgOperation.convertAndSend("/sub/chat/room/"+chatRequestDto.getChatRoomId(),chatResponseDto);
        notificationService.notify(chatResponseDto.getReceiverId(),chatDetailResponseDto);
    }

    //그동안의 채팅내역들이 보여지고 , 읽지않았던 채팅들이 있으면 읽음으로 상태를 바꾼다.


    @GetMapping("/chatRoom/enter/{receiverId}")
    public ResponseEntity<ChatRoomResponseDto> enterChatRoom(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long receiverId){
        return chatService.enterChatRoom(userDetails.getMember(),receiverId);
    }

    /*
    1. pub/chat/send 준비 , 보낼데이터(채팅) 첫채팅이면 chatRoomId = null
    2. /sub/chat/room 여기로 데이터가 날아옴 , chatRoomId를 줌 , 그 받음 chatRoomId를 이제 가지고 있어야함
    3. /sub/chat/room/(받은 chatRoomId)에 구독
    4. /sub/chat/room/(chatRoomId)에서 받은 데이터를 가지고 , /pub/chat/read에 그대로 보내기
     */

    @MessageMapping("/chat/read")
    public void readChat(ChatResponseDto chatResponseDto){
        chatService.markAsRead(chatResponseDto);
    }

}
