package com.ssafy.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.dto.user.User;
import com.ssafy.dto.user.UserLoginRequest;
import com.ssafy.dto.user.UserSignupRequest;
import com.ssafy.jwt.CustomOAuth2User;
import com.ssafy.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final UserService service;
	
	@GetMapping("/me")
	public ResponseEntity<?> me(Authentication authentication) {
		if(authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
		Map<String, String> response = new HashMap<>();
		response.put("name", user.getName());
		response.put("email", user.getEmail());
		return ResponseEntity.ok(Map.of("name", user.getName(), "email", user.getEmail()));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody UserSignupRequest user) {
		try {
			service.signup(user);
			return ResponseEntity.ok("회원가입 성공");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserLoginRequest user, HttpServletResponse response) {
		try {
			String token = service.login(user.getLoginId(), user.getPassword());
			User userData =service.findByLoginId(user.getLoginId());
			Cookie cookie = new Cookie("Authorization", token);
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			cookie.setMaxAge(60*60*6000);
			response.addCookie(cookie);
			
			Map<String, Object> responseBody = new HashMap<>();
	        responseBody.put("status", "ok");
	        responseBody.put("message", Map.of("name", userData.getName(), "email", userData.getEmail()));

//	        return ResponseEntity.ok(responseBody);
	        return ResponseEntity.ok(Map.of("name", userData.getName(), "email", userData.getEmail()));
		} catch (BadCredentialsException e) {
			Map<String, String> error = new HashMap<>();
	        error.put("status", "fail");
	        error.put("message", "아이디 또는 비밀번호가 잘못되었습니다.");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
		}
	}
	
	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpServletResponse response) {
		Cookie cookie = new Cookie("Authorization", null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
		System.out.println("logout");
		return ResponseEntity.ok("로그아웃 ");
	}
}
