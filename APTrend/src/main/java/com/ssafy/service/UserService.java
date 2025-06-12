package com.ssafy.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.dto.user.User;
import com.ssafy.dto.user.UserAuth;
import com.ssafy.dto.user.UserSignupRequest;
import com.ssafy.jwt.JWTUtil;
import com.ssafy.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository repository;
	private final PasswordEncoder pe;
	private final JWTUtil jwtUtil;
	
	public void signup(UserSignupRequest usr) {
		String email = usr.getEmail();
		User receivedUser = repository.findByEmail(email);
		System.out.println("this is service : "+usr);
		if(receivedUser!=null) {
			if(repository.findById(receivedUser.getId())>0) {
				throw new IllegalArgumentException("이미 존재하는 사용자 입니다.");
			} else {
				UserAuth ua = UserAuth.builder()
						.userId(receivedUser.getId())
						.provider("local")
						.providerId("local "+usr.getLoginId())
						.loginId(usr.getLoginId())
						.password(pe.encode(usr.getPassword()))
						.build();
				repository.createUserAuth(ua);
				return ;
			}
		} else {
			User insertData = User.builder()
					.name(usr.getName())
					.email(usr.getEmail())
					.phoneNumber(usr.getPhoneNumber())
					.address(usr.getAddress())
					.role("ROLE_USER")
					.isVerified(false)
					.build();
			repository.createUser(insertData);
			receivedUser = repository.findByEmail(email);
			UserAuth ua = UserAuth.builder()
					.userId(receivedUser.getId())
					.provider("local")
					.providerId("local "+usr.getLoginId())
					.loginId(usr.getLoginId())
					.password(pe.encode(usr.getPassword()))
					.build();
			repository.createUserAuth(ua);
		}
	}
	
	public String login(String loginId, String password) {
		UserAuth user = repository.findByLocalLoginId(loginId);
		if(user==null) {
			throw new BadCredentialsException("사용자 없음");
		}
		
		if(!pe.matches(password, user.getPassword())) {
			throw new BadCredentialsException("비밀번호 불일치");
		}
		
		User userData = repository.findByIdReturnUser(user.getUserId());
		
		return jwtUtil.createJwt(userData.getName(), userData.getEmail(), user.getLoginId(), userData.getRole(), 60*60*60L );
	}
	
	public int findUserIdByEmail(String email) {
		return repository.findUserIdByEmail(email);
	}
	public User findUserByEmail(String email) {
		return repository.findUserByEmail(email);
	}
	public User findByLoginId(String loginId) {
		return repository.findByLoginId(loginId);
	}
}
