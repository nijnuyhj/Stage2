package com.example.Week2.service;

import com.example.Week2.dto.request.ChatRequestDto;
import com.example.Week2.dto.response.ChatDetailResponseDto;
import com.example.Week2.dto.response.ChatResponseDto;
import com.example.Week2.dto.response.ChatRoomInfoResponseDto;
import com.example.Week2.dto.response.ChatRoomResponseDto;
import com.example.Week2.entity.Chat;
import com.example.Week2.entity.Member;
import com.example.Week2.entity.ChatRoom;
import com.example.Week2.repository.ChatRepository;
import com.example.Week2.repository.ChatRoomRepository;
import com.example.Week2.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatService {


    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;

    public ResponseEntity<Queue<ChatRoomInfoResponseDto>> findAllChatRoom(Member member) {
        Queue<ChatRoomInfoResponseDto> chatRoomInfoResponseDtoQueue = new PriorityQueue<>(Comparator.comparing(ChatRoomInfoResponseDto::getLastChatTime));
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByHostOrGuest(member, member);

        for (ChatRoom chatRoom : chatRoomList) {
            Optional<Chat> chatOptional = chatRepository.findTopByChatRoomIdAndCreatedAtAfterOrderByCreatedAtDesc(chatRoom.getId(), chatRoom.getHostEntryTime());

            if (chatOptional.isPresent()) {
                Chat chat = chatOptional.get();
                boolean isCurrentUserHost = chatRoom.getHost().getId().equals(member.getId());

                boolean isUnread = chat.getReceiverId().equals(member.getId()) && !chat.getReadStatus();

                ChatRoomInfoResponseDto chatRoomInfoResponseDto = new ChatRoomInfoResponseDto(
                        chatRoom.getId(),
                        chat.getCreatedAt(),
                        ChatResponseDto.from(chat),
//                        isCurrentUserHost ? chatRoom.getGuest().getProfileImg() : chatRoom.getHost().getProfileImg(),
                        isCurrentUserHost ? chatRoom.getGuest().getId() : chatRoom.getHost().getId(),
                        isCurrentUserHost ? chatRoom.getGuest().getUsername() : chatRoom.getHost().getUsername(),
//                        isCurrentUserHost ? chatRoom.getGuest().getRole() : chatRoom.getHost().getRole(),
                        isUnread
                );

                chatRoomInfoResponseDtoQueue.add(chatRoomInfoResponseDto);
            }
        }

        return ResponseEntity.ok(chatRoomInfoResponseDtoQueue);
    }

    public Long createChatRoom(Long hostId, Long guestId) {
        Member userOne = memberRepository.findById(hostId).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
        Member userTwo = memberRepository.findById(guestId).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
        ChatRoom chatRoom = ChatRoom.of(userOne, userTwo, LocalDateTime.now().plusHours(9));
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return savedChatRoom.getId();
    }

    public ChatResponseDto saveChat(ChatRequestDto chatRequestDto) {
        Chat chat = ChatRequestDto.toEntity(chatRequestDto, LocalDateTime.now().plusHours(9));
        chatRepository.save(chat);
        return ChatResponseDto.from(chat);
    }
    public ChatDetailResponseDto createChatDetailResponseDto(ChatResponseDto chatResponseDto) {
        Member sender = memberRepository.findById(chatResponseDto.getSenderId()).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
        return ChatDetailResponseDto.from(chatResponseDto,sender);
    }

    public ChatService(MemberRepository memberRepository, ChatRoomRepository chatRoomRepository, ChatRepository chatRepository) {
        this.memberRepository = memberRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatRepository = chatRepository;
    }

    public ResponseEntity<ChatRoomResponseDto> enterChatRoom(Member userOne, Long userTwoId) {
        Member userTwo = memberRepository.findById(userTwoId).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습ㄴ다"));
        Optional<ChatRoom> chatRoom = chatRoomRepository.findChatRoomByMember(userOne.getId(), userTwoId);
        ChatRoomResponseDto chatRoomResponseDto;

        if (chatRoom.isPresent()) {
            ChatRoom findChatRoom = chatRoom.get();

            chatRepository.saveChat(findChatRoom.getId(), LocalDateTime.now());

            List<Chat> chatList;
            // MongoDB 대신 MySQL에서 업데이트 쿼리 실행
            chatRepository.updateChatReadStatus(findChatRoom.getId(), userTwo.getId());

            if (findChatRoom.getGuest().getId().equals(userOne.getId())) {
                // MongoDB 대신 MySQL에서 채팅 조회
                chatList = chatRepository.findByChatRoomIdAndCreatedAtAfter(findChatRoom.getId(), findChatRoom.getGuestEntryTime());
            } else {
                // MongoDB 대신 MySQL에서 채팅 조회
                chatList = chatRepository.findByChatRoomIdAndCreatedAtAfter(findChatRoom.getId(), findChatRoom.getHostEntryTime());
            }

            List<ChatResponseDto> chatListDto = chatList.stream().map(ChatResponseDto::from).collect(Collectors.toList());
            chatListDto.sort(Comparator.comparing(ChatResponseDto::getCreatedAt));

            chatRoomResponseDto = new ChatRoomResponseDto(findChatRoom.getId(),
                    chatListDto,
//                    userTwo.getProfileImg(),
                    userTwoId,
                    userTwo.getUsername());
            return ResponseEntity.ok(chatRoomResponseDto);
        } else {
            Long chatRoomId = createChatRoom(userOne.getId(), userTwoId);
            chatRoomResponseDto = new ChatRoomResponseDto(chatRoomId, new ArrayList<>(), userTwoId, userTwo.getUsername());
            return ResponseEntity.ok(chatRoomResponseDto);
        }
    }

    public ResponseEntity<String> markAsRead(ChatResponseDto chatResponseDto) {
        // Chat 객체를 DB에서 가져오거나 다른 저장소에서 가져와서 업데이트
        Optional<Chat> optionalChat = chatRepository.findById(chatResponseDto.getUuid());

        if (optionalChat.isPresent()) {
            Chat chat = optionalChat.get();
            chat.updateReadStatus();
            chatRepository.save(chat); // 또는 업데이트할 저장소에 따라 다른 메서드 사용
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chat not found");
        }
    }

    public ResponseEntity<Boolean> checkUnReadChat(Member member) {
        List<ChatRoom> findAllChatRoom = chatRoomRepository.findAllByHostOrGuest(member, member);

        // 레디스를 사용하지 않으므로 DB에서 직접 조회
        boolean haveToRead = chatRepository.existsByReceiverIdAndReadStatus(member.getId(), false);

        return ResponseEntity.ok(haveToRead);
    }

    public ResponseEntity<String> saveChatList(Long chatRoomId) {
        saveChatsToDB(chatRoomId); // 이 메서드는 실제로는 다른 저장소로 채팅을 저장하는 로직을 구현해야 합니다.

        return ResponseEntity.ok("success");
    }

    private void saveChatsToDB(Long chatRoomId) {
        List<Chat> chatList = chatRepository.findByChatRoomId(chatRoomId);
        // DB에 저장하는 로직 추가
    }

}