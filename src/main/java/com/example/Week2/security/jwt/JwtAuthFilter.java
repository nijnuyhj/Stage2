package com.example.Week2.security.jwt;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtAuthFilter extends GenericFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil)  {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (jwtUtil != null) { // jwtUtil이 null이 아닌지 확인
            // 1. Request Header 에서 토큰을 꺼냄
            String jwt = jwtUtil.resolveToken((HttpServletRequest) request);
            if (jwt != null && jwtUtil.validateToken(jwt)) {   // token 검증
                Authentication auth = jwtUtil.getAuthentication(jwt);    // 인증 객체 생성
                SecurityContextHolder.getContext().setAuthentication(auth); // SecurityContextHolder에 인증 객체 저장
            }
        }
        chain.doFilter(request, response);
    }
}