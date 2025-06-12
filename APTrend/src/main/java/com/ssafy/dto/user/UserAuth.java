package com.ssafy.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAuth {
	private int authId;
	private int userId;
	private String provider;
	private String providerId;
	private String loginId;
	private String password;
}
