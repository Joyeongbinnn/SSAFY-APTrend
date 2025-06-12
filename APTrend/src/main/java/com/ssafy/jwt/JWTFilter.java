package com.ssafy.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ssafy.dto.OAuth2UserDto;
import com.ssafy.dto.user.User;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        // ✅ 인증 제외 경로는 바로 통과
        if (uri.startsWith("/api/auth/signup") ||
            uri.startsWith("/api/auth/login") ||
            uri.startsWith("/api/email")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Authorization".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (jwtUtil.isExpired(token)) {
                System.out.println("토큰 만료됨");
                filterChain.doFilter(request, response);
                return;
            }

            String name = jwtUtil.getName(token);
            String email = jwtUtil.getEmail(token);
            String loginId = jwtUtil.getLoginId(token);
            String role = jwtUtil.getRole(token);

            OAuth2UserDto userDto = OAuth2UserDto.builder()
                    .name(name)
                    .email(email)
                    .loginId(loginId)
                    .role(role)
                    .build();

            CustomOAuth2User principal = new CustomOAuth2User(userDto);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    principal, null, principal.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (ExpiredJwtException e) {
            // 예외 발생 시도 그냥 넘김 (필터 통과)
            System.out.println("만료된 JWT 토큰 사용 시도: " + e.getMessage());
            filterChain.doFilter(request, response); // ✅ 이게 핵심
            return;
        } catch (Exception e) {
            System.out.println("JWTFilter error: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
