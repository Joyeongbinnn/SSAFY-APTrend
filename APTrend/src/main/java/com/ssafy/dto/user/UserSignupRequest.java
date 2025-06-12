package com.ssafy.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSignupRequest {
	private String name;
	private String email;
	private String loginId;
	private String password;
	private String phoneNumber;
	private String address;
}
