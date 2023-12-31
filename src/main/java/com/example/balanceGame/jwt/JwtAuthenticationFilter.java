package com.example.balanceGame.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Order(0)
@Slf4j
@Component
// 한 번에 요청에 한 번만 동작하는 필터
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final JwtProvider jwtProvider;
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {
                "/swagger-ui.html",
                "/swagger-ui/index.html",
                "/swagger-ui/index.css",
                "/swagger-ui/swagger-ui.css",
                "/v3/api-docs",
                "/swagger-ui/swagger-ui-standalone-preset.js",
                "/swagger-ui/swagger-ui-bundle.js",
                "/swagger-ui/swagger-initializer.js",
                "/swagger-ui/favicon-32x32.png",
                "/swagger-ui/favicon-16x16.png",
                "/user/join",
                "/user/login"};

        // 제외할 url 설정
        String path = request.getRequestURI();
        if (Arrays.stream(excludePath).anyMatch(path::startsWith)) {
            return true; // Swagger 요청이면 필터링하지 않음
        }

        return false;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
        }else{
            // Authentication 헤더 추출
            String token = jwtProvider.resolveToken(request);

            // 토큰이 헤더에 담겨있을 때
            if (token != null) {
                // Bearer 분리
                String[] slice = token.split(" ");

                // 인증 객체 생성
                Authentication authentication = jwtProvider.getAuthentication(slice[1]);

                // 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden 에러 응답
            }
        }
    }
}
