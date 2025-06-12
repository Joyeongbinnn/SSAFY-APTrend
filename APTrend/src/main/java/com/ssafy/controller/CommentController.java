package com.ssafy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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

import com.ssafy.dto.board.Comment;
import com.ssafy.dto.user.User;
import com.ssafy.jwt.CustomOAuth2User;
import com.ssafy.service.CommentService;
import com.ssafy.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
	private final CommentService service;
	private final UserService userService;
	
	@PostMapping("/list/{boardId}")
	public ResponseEntity<List<Comment>> getBoardList(@PathVariable int boardId) {
		List<Comment> list = service.commentList(boardId);
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Comment> getCommentDetail(@PathVariable int id) {
		Comment detail = service.detailComment(id);
		return ResponseEntity.ok(detail);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<Void> updateComment(@PathVariable int id, @RequestBody Comment comment, @AuthenticationPrincipal CustomOAuth2User user) {
		int userId = userService.findUserIdByEmail(user.getEmail());
		int result = service.updateComment(comment, userId);
		if(result>0)
			return ResponseEntity.ok(null);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PostMapping("/create")
	public ResponseEntity<Void> createComment(@RequestBody Comment comment, @AuthenticationPrincipal CustomOAuth2User user) {
		int userId = userService.findUserIdByEmail(user.getEmail());
		comment.setBoardUserId(userId);
		int result = service.createBoard(comment);
		if(result>0)
			return ResponseEntity.ok(null);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@DeleteMapping("/{boardId}/{id}")
	public ResponseEntity<Void> deleteComment(@PathVariable Integer id, @PathVariable Integer boardId, @AuthenticationPrincipal CustomOAuth2User user) {
		System.out.println(id);
		int userId = userService.findUserIdByEmail(user.getEmail());
		int result = service.deleteComment(id, boardId);
		if(result>0)
			return ResponseEntity.ok(null);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
