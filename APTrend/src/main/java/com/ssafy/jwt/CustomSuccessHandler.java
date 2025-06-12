package com.ssafy.jwt;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	private final JWTUtil jwtUtil;
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
		String name = customUserDetails.getName();
		String email = customUserDetails.getEmail();
		String loginId = customUserDetails.getLoginId();
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		String role = auth.getAuthority();
		System.out.println("로그인 성공");
		String token = jwtUtil.createJwt(name, email, loginId, role, 60*60*6000L);
		
		response.addCookie(createCookie("Authorization", token));
		System.out.println("쿠키 생성 성공");
		response.setHeader("Set-Cookie",
			    String.format("Authorization=%s; Path=/; Max-Age=%d; HttpOnly; Secure; SameSite=None",
			                  token, 60 * 60 * 6000));
		response.sendRedirect("http://192.168.205.82:5173");
		System.out.println("리다이렉트 성공");
	}
	
	private Cookie createCookie(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(60*60*6000);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		
		return cookie;
	}
}
