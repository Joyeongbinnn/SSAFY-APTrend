package com.ssafy.dto;

import lombok.Data;

@Data
public class GptResponse {
	private String title;
	private String content;
	private Object prediction;
}
