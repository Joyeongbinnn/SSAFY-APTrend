package com.ssafy.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {
	private int id;
	private int boardId;
	private int boardUserId;
	private String writer;
	private String content;
	private int parentId;
	private String createdAt;
}
