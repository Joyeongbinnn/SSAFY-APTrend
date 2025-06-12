package com.ssafy.service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender mailSender;
	private final Map<String, String> codeStore = new ConcurrentHashMap<>();
	
	public void sendVerificationCode(String email) {
		String code = String.format("%06d", new Random().nextInt(1000000));
		codeStore.put(email, code);
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setFrom("junjun32918@naver.com");
		message.setSubject("이메일 인증코드");
		message.setText("인증코드 : "+code);
		mailSender.send(message);
	}
	
	public boolean verifyCode(String email, String inputCode) {
		return inputCode.equals(codeStore.get(email));
	}
}
