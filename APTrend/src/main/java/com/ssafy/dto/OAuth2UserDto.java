package com.ssafy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuth2UserDto {
	private String name;
	private String email;
	private String provider;
	private String providerId;
	private String loginId;
	private String role;
}
