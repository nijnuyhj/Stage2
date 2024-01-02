package com.example.Week2.controller;

import com.example.Week2.dto.request.MemberRequestDto;
import com.example.Week2.dto.response.ResponseMessage;
import com.example.Week2.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage<Void>>signUp(@RequestBody MemberRequestDto memberRequestDto){
        memberService.signUp(memberRequestDto);
        return new ResponseEntity<>(new ResponseMessage("회원가입 성공",null), HttpStatus.CREATED);
    }

}
