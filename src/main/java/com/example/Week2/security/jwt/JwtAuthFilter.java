package com.example.Week2.security.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            String uri = request.getRequestURI();

            if(uri.contains("api/signup")|| uri.contains("api/login")){
                filterChain.doFilter(request,response);
                return;
            }

            String accessToken = jwtUtil.resolveToken(request, AUTHORIZATION_ACCESS);
            String refreshToken = jwtUtil.resolveToken(request, AUTHORIZATION_REFRESH);

            //인증 필요 없거나 refreshToken이 있을 경우 (토큰 재발행 요청) 다음 필터로 이동한다.
            if(accessToken == null || refreshToken!=null){
                filterChain.doFilter(request,response);
                return;
            }

            jwtUtil.validateAccessToekn(request,response);
            Claims info = jwtUtil.getUserInfoFromToken(token,false);
            setAuthentication(info.getSubject());
            filterChain.doFilter(request,response);
        }

        //인증/인가 설정
        private void setAuthentication(String username){
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(username);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
