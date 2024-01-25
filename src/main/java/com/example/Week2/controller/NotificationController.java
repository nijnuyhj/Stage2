package com.example.Week2.controller;

import com.example.Week2.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping(value = "/chat/alarm/{memberId}", produces = "text/event-stream")
    public SseEmitter chatAlarm(@PathVariable Long memberId, HttpServletResponse response) {
        return notificationService.subscribe(memberId);
    }
}