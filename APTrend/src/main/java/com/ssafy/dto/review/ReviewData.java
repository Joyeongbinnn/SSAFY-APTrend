package com.ssafy.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewData {
	private int userId;
	private String userName;
	private String apartId;
	private String apartName;
	private String address;
	private String content;
	private float rate;
}
