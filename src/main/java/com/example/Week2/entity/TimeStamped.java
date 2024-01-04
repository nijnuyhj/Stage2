package com.example.Week2.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class TimeStamped {
    @CreatedDate// 생성일자임을 나타냄
    private LocalDateTime createdAt; // LocalDateTime은 시간을 나타내는 자료형

    @LastModifiedDate// 마지막 수정일자임을 나타냄
    private LocalDateTime modifiedAt;
}
