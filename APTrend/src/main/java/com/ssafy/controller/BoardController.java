package com.ssafy.controller;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ssafy.dto.GptResponse;
import com.ssafy.dto.PageDto;
import com.ssafy.dto.QuestionRequest;
import com.ssafy.dto.board.Board;
import com.ssafy.dto.user.User;
import com.ssafy.jwt.CustomOAuth2User;
import com.ssafy.service.BoardService;
import com.ssafy.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {
	private final BoardService service;
	private final UserService userService;
	private final RestTemplate restTemplate = new RestTemplate();
	
	@PostMapping("/list")
	public ResponseEntity<List<Board>> getBoardList(@RequestBody PageDto page) {
		List<Board> list = service.boardList(page.getPage()-1);
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Board> getBoardDetail(@PathVariable int id) {
		Board detail = service.detailBoard(id);
		return ResponseEntity.ok(detail);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<Void> updateBoard(@PathVariable int id, @RequestBody Board board, @AuthenticationPrincipal CustomOAuth2User user) {
		int userId = userService.findUserIdByEmail(user.getEmail());
		int result = service.updateBoard(board, userId);
		if(result>0)
			return ResponseEntity.ok(null);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PostMapping("/create")
	public ResponseEntity<Void> createBoard(@RequestBody Board board, @AuthenticationPrincipal CustomOAuth2User user) {
		User userData = userService.findUserByEmail(user.getEmail());
		board.setUserId(userData.getId());
		board.setUserName(userData.getUsername());
		int result = service.createBoard(board);
		if(result>0)
			return ResponseEntity.ok(null);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBoard(@PathVariable int id, @AuthenticationPrincipal CustomOAuth2User user) {
		int userId = userService.findUserIdByEmail(user.getEmail());
		int result = service.deleteBoard(id, userId);
		if(result>0)
			return ResponseEntity.ok(null);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	@GetMapping("/AI")
	public ResponseEntity<Void> createAIBoard() throws RestClientException {
		int userId = userService.findUserIdByEmail("AI@AI");
		String fastApiUrl = "http://localhost:8000/ask";
		QuestionRequest request = new QuestionRequest("이번 주 부동산에 관해서 가장 큰 이슈를 알려줘");
		// 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<QuestionRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<GptResponse> response = restTemplate.postForEntity(
                fastApiUrl, entity, GptResponse.class
        );
        Board board = Board.builder()
        		.userId(userId)
        		.userName("AI")
        		.title(response.getBody().getTitle())
        		.content(response.getBody().getContent())
        		.build();
        service.createBoard(board);
		return ResponseEntity.ok(null);
	}
}
