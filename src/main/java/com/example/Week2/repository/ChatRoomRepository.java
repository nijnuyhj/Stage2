package com.example.Week2.repository;

import com.example.Week2.entity.ChatRoom;
import com.example.Week2.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    List<ChatRoom> findAllByHostOrGuest(Member host, Member guest);
    @Query("select c from ChatRoom c where (c.host.id = :member1 and c.guest.id = :member2) or (c.host.id =:member2 and c.guest.id=:member1)")
    Optional<ChatRoom> findChatRoomByMember(@Param("member1") Long member1, @Param("member2") Long member2);
}
