package com.ssafy.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserJoinUserAuth {
	private int id;
	private String username;
	private String name;
	private String email;
	private String phoneNumber;
	private String address;
	private String role;
	private boolean isVerified;
	private String createdAt;
	private String provider;
	private String providerId;
	private String loginId;
	private String password;
}
