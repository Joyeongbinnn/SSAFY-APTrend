package com.ssafy.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.service.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {
	private final EmailService service;
	
	@PostMapping("/send-code")
	public ResponseEntity<?> sendCode(@RequestBody Map<String, String> body) {
		String email = body.get("email");
		service.sendVerificationCode(email);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/verify-code")
	public ResponseEntity<Boolean> verifyCode(@RequestBody Map<String, String> body) {
		String email = body.get("email");
		String code = body.get("code");
		boolean result = service.verifyCode(email, code);
		return ResponseEntity.ok(result);
	}
}
