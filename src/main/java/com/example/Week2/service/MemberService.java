package com.example.Week2.service;

import com.example.Week2.dto.request.LoginRequestDto;
import com.example.Week2.dto.request.MemberRequestDto;
import com.example.Week2.dto.request.TokenRequestDto;
import com.example.Week2.dto.response.MemberResponseDto;
import com.example.Week2.entity.Member;
import com.example.Week2.entity.RefreshToken;
import com.example.Week2.repository.MemberRepository;
import com.example.Week2.repository.RefreshTokenRepository;
import com.example.Week2.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberResponseDto signUp(MemberRequestDto memberRequestDto) throws IllegalAccessException{
        String username = memberRequestDto.getUsername();
        String password = memberRequestDto.getPassword();
        String confirm = memberRequestDto.getConfirm();
        Optional<Member> found = memberRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalAccessException("중복 닉네임 확인!");
        }
        if (!password.equals(confirm)) {
            throw new IllegalAccessException("비밀번호가 서로 다릅니다!");
        }
        // 패스워드 암호화
        password = passwordEncoder.encode(memberRequestDto.getPassword());
        LoginRequestDto dto = LoginRequestDto.builder()
                .username(username)
                .password(password)
                .build();

        Member member = new Member(dto);
        memberRepository.save(member);

        MemberResponseDto responseDto = new MemberResponseDto(member);

        return responseDto;
    }
    // 로그인
    @Transactional
    public MemberResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
//        Optional <User> id = userRepository.findByNickname(loginRequestDto.getNickname());
//        if(!id.get().getPassword().equals(loginRequestDto.getPassword())){
//            return Optional.empty();
//        }
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenRequestDto tokenRequestDto = jwtUtil.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenRequestDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        response.addHeader("Access-Token", tokenRequestDto.getGrantType()+" "+tokenRequestDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenRequestDto.getRefreshToken());
        // 로그인시 user 정보 뿌려주고싶음
        Member member = memberRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow( () -> new UsernameNotFoundException("유저를 찾을 수 없습니다"));

        MemberResponseDto memberResponseDto = new MemberResponseDto(member);

        return memberResponseDto;
    }
}
