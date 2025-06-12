package com.ssafy.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Board {
	private int id;
	private int userId;
	private String userName;
	private String title;
	private String createdAt;
	private String tag;
	private String summary;
	private String content;
	private int commentCount;
	private int recommend;
}
