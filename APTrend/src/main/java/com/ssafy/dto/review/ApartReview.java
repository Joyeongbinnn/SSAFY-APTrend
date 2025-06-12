package com.ssafy.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApartReview {
	private String apartId;
	private String address;
	private String name;
	private String averageRating;
	private String reviewCount;
}
