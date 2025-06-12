package com.ssafy.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.ssafy.dto.OAuth2UserDto;
import com.ssafy.dto.user.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User{
	private final OAuth2UserDto user;
	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<>();
		collection.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return user.getRole();
			}
			
		});
		
		return collection;
	}

	@Override
	public String getName() {
		return user.getName();
	}
	
	public String getEmail() {
		return user.getEmail();
	}
	
	public String getLoginId() {
		return user.getLoginId();
	}
	
	@Override
	public String toString() {
		return "CustomOAuth2User{" +
	            "name=" + (user!=null?user.getName():"null") +
	            ", loginId=" + user.getLoginId() +
	            ", role=" + user.getRole() +
	            ", email=" + user.getEmail() +
	            '}';
	}
}
