package com.example.Week2.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EmitterRepository {

    // 인터페이스 내에서 필드는 기본적으로 public, static, final이어야 합니다.
    Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    /**
     * Save the given ID and emitter
     *
     * @param id      - User ID.
     * @param emitter - Event Emitter.
     */
    public void save(Long id, SseEmitter emitter) {
        emitters.put(id, emitter);
    }

    /**
     * Remove emitter of given ID
     *
     * @param id - User ID.
     */
    public void deleteById(Long id) {
        emitters.remove(id);
    }

    /**
     * Retrieves the emitter of the given ID.
     *
     * @param id - User ID.
     * @return SseEmitter - Event Emitter.
     */
    public SseEmitter get(Long id) {
        return emitters.get(id);
    }
}