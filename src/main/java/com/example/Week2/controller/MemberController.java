package com.example.Week2.controller;

import com.example.Week2.dto.request.LoginRequestDto;
import com.example.Week2.dto.request.MemberRequestDto;
import com.example.Week2.dto.response.MemberResponseDto;
import com.example.Week2.dto.response.ResponseMessage;
import com.example.Week2.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public MemberResponseDto signUp(@RequestBody @Valid MemberRequestDto memberRequestDto)throws IllegalAccessException{
        return memberService.signUp(memberRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage<MemberResponseDto>>login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){
        MemberResponseDto memberResponse = memberService.login(loginRequestDto,response);
        return new ResponseEntity<>(new ResponseMessage<>("로그인 성공",memberResponse),HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<ResponseMessage<String>>testtest(){
        return new ResponseEntity<>(new ResponseMessage<>("asdasdasdasd",null),HttpStatus.OK);
    }


}
