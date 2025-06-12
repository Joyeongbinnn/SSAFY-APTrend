package com.ssafy.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.ssafy.dto.OAuth2UserDto;
import com.ssafy.dto.user.User;
import com.ssafy.dto.user.UserAuth;
import com.ssafy.jwt.CustomOAuth2User;
import com.ssafy.repository.UserRepository;
import com.ssafy.response.GoogleResponse;
import com.ssafy.response.KakaoResponse;
import com.ssafy.response.NaverResponse;
import com.ssafy.response.OAuth2Response;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService{
	private final UserRepository repository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		
		System.out.println(oAuth2User);
		
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		OAuth2Response oAuth2Response = null;
		
		if(registrationId.equals("naver")) {
			oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
		} else if(registrationId.equals("google")) {
			oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
		} else if(registrationId.equals("kakao")) {
			oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
		} else {
			return null;
		}
		
		String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
		User existData = repository.findByLoginId(username);
		
		if(existData == null) {
			if(repository.findByEmailReturnInt(oAuth2Response.getEmail()) == 0) { 
				existData = User.builder()
						.username(username)
						.email(oAuth2Response.getEmail())
						.name(oAuth2Response.getName())
						.phoneNumber("010-0000-0000")
						.address(null)
						.role("ROLE_USER")
						.isVerified(false)
						.build();
				repository.createUser(existData);
			}
			existData = repository.findByEmail(oAuth2Response.getEmail());
			UserAuth auth = UserAuth.builder()
					.userId(existData.getId())
					.provider(oAuth2Response.getProvider())
					.providerId(oAuth2Response.getProviderId())
					.loginId(username)
					.password(null)
					.build();
			repository.createUserAuth(auth);
		}
		System.out.println("받은거"+existData);
		OAuth2UserDto userDto = OAuth2UserDto.builder()
				.loginId(username)
				.name(existData.getName())
				.email(existData.getEmail())
				.role(existData.getRole())
				.build();
		System.out.println("만든거"+userDto);
		return new CustomOAuth2User(userDto);
	}
}
