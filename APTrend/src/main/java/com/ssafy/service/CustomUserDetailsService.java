package com.ssafy.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ssafy.dto.user.UserJoinUserAuth;
import com.ssafy.repository.UserRepository;
import com.ssafy.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
	private final UserRepository repository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserJoinUserAuth userData = repository.findLocalDataByLoginId(username);
		if(userData!=null) {
			return new CustomUserDetails(userData);
		}
		return null;
	}

}
